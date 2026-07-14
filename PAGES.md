# Koinvois — Dokumentasi Halaman (Pages)

Dokumen ini berdasarkan pembacaan kode AKTUAL (per 2026-07-12), BUKAN dari `feature.md` lama yang sudah usang (ditulis sebelum Dashboard/onboarding/MainActivity direstruktur ulang secara signifikan di luar sesi-sesi sebelumnya). Setiap kali app ini berubah lagi, dokumen ini perlu diverifikasi ulang terhadap kode, jangan dipercaya buta.

## Ringkasan jumlah halaman

| Modul | Jumlah | Catatan |
|---|---|---|
| Splash/Onboarding & Lock/PIN | 5 | 3 di antaranya (`LockMainFragment`/`SavePasswordFragment`) shared dengan modul Settings |
| Dashboard | 1 | Redesain besar: greeting, notification bell (dekoratif, belum ada listener), Overview cards, Recent Invoices, Quick Actions, FAB |
| Invoice | 20 | List (tab All/Outstanding/Paid) + wizard lengkap (Edit/Preview/History + 12 sub-halaman) |
| Estimate | 12 (nav graph) + 6 (tab ViewPager2 saja) = 18 | Mirror 1:1 pola Invoice, minus fitur due-date/terms & fitur pembayaran |
| Client | 2 | List + Add/Edit |
| Item | 2 | List + Add/Edit |
| Reports | 3 | Shell tab (Paid/Clients) |
| Settings | 2 murni (`SettingFragment`, `AddBusinessDetailsFragment`) + 2 shared (Lock, SavePassword sudah dihitung di Splash) | |
| More | 1 | Redesain besar: 11 menu item, hanya 3 yang benar-benar berfungsi |
| **Total** | **54** | |

**Sengaja tidak didokumentasikan:** `HomeFragment` dan `NotificationsFragment` — dikonfirmasi tidak tersambung ke navigation graph manapun, sisa scaffolding default Android Studio, bukan halaman nyata di app.

## Catatan arsitektur navigasi penting (berlaku lintas-halaman)

- **Shell UI** (`activity_main.xml` + `MainActivity.kt`): header baru (ikon Settings + greeting "Hello, John" + judul + notification bell dengan badge "3" — bell ini **dekoratif, tidak ada click listener**) dan `BottomNavigationView` diposisikan **di atas** (overlap header), bukan di bawah layar. Toolbar lama (`include layout="toolbar"`) masih ada di XML tapi `visibility="gone"` permanen — banyak Fragment level-dalam (Client/Item/Reports/Settings/dst, dan HAMPIR SEMUA halaman wizard Invoice/Estimate) masih memanggil `(activity as MainActivity).binding?.toolbar` untuk mengatur judul/tombol back — kode ini join ke toolbar yang sudah invisible, sehingga toolbar/back-button/judul di halaman-halaman tsb kemungkinan **tidak tampil secara visual** kecuali header baru kebetulan menaunginya. Ini pengamatan struktural, bukan sesuatu yang diperbaiki di dokumen ini.
- Header baru (greeting+bell) hanya disembunyikan (`headerContainer.hide()`) untuk 32 destination yang didaftarkan eksplisit di `MainActivity`'s `addOnDestinationChangedListener` (kebanyakan halaman wizard Invoice/Estimate + Lock/Splash). Untuk destination lain (termasuk Client/Item/Reports/Settings list), header baru TETAP tampil.
- `txtGreeting` ("Hello, John") hanya tampil kalau destination == `dashboard_navigation_graph`; disembunyikan di tab lain, tapi `txtMainTitle` berubah sesuai 5 top-level graph (Dashboard/Invoice/Estimate/Reports/More).
- **InvoiceMainFragment** (tapi TIDAK EstimatesMainFragment) meng-intercept tombol Back dengan dialog custom "Exit App?" (`exit_dialog.xml`) — kalau user berada di tab Invoice lalu tekan Back, muncul dialog konfirmasi keluar app, alih-alih behavior standar Android. Ini legacy behavior yang tidak konsisten dengan tab lain.

---

## A. Splash, Onboarding & Kunci PIN

### 1. Splash / Onboarding (`SplashMainFragment`)
**Fitur:**
- Cek status `isFirstTime` & `lockMode` dari DataStore saat halaman dibuka — kalau first-time, tampilkan onboarding 3-slide (ViewPager2); kalau bukan first-time tapi lock aktif, langsung ke Enter PIN; kalau tidak keduanya, langsung ke Dashboard (tanpa splash tampil).
- Onboarding: 3 slide ilustrasi dengan judul+deskripsi (`onboarding_title_1/2/3`), indikator dot aktif/tidak-aktif, tombol Skip (langsung lanjut), Next (geser slide), dan Get Started (muncul di slide terakhir).
**Widget/Komponen:** `ViewPager2` (adapter `OnboardingAdapter`), `LinearLayout` indikator dot dinamis, `TextView` Skip, `AppCompatImageView` Next (ikon panah), `MaterialButton` Get Started (visibility gone/visible bergantian). Background selalu gelap (`color_onboarding_background`, fixed, tidak ikut tema app).
**User Story:** Sebagai pemilik bisnis baru pertama kali buka app, saya ingin melihat pengenalan singkat fitur invoice supaya paham apa yang bisa dilakukan app ini sebelum mulai memakainya.
**Navigasi:** Entry: satu-satunya pintu masuk app (start destination `MainActivity.onCreate()` navigasi paksa ke sini kalau `savedInstanceState == null`). Keluar: ke `AddBusinessDetailsSplashFragment` (first-time), ke `EnterPasswordFragment` (lock aktif), atau langsung ke `dashboard_navigation_graph` (popUpTo splash inclusive, sehingga splash hilang dari back-stack).

### 2. Setup Profil Bisnis Awal (`AddBusinessDetailsSplashFragment`)
**Fitur:** Form 3-step (indikator step 1/2/3 di atas) untuk isi profil bisnis pertama kali: nama, alamat, kontak, email, telepon, logo (pilih dari galeri/kamera via bottom sheet). Preview nama & alamat live-update saat mengetik. Tombol back per-step (kembali 1 step) atau keluar fragment kalau di step 1.
**Widget/Komponen:** 3 `View` step (`firstBusinessView`/`secondBusinessView`/`thirdBusinessView`), indikator step (`txtStep1/2/3`, background+warna berubah sesuai step aktif), `ImageView` logo + `BottomSheetDialog` (`bottom_sheet_logo`) untuk pilih Camera/Gallery, `EditText` banyak field, `btnCreateInvoice` (submit akhir).
**User Story:** Sebagai pemilik bisnis baru, saya ingin mengisi profil bisnis saya (nama, logo, alamat) di awal supaya semua invoice/estimate yang saya buat nanti otomatis menampilkan identitas bisnis saya.
**Navigasi:** Entry: dari SplashMainFragment (jalur first-time). Keluar: submit (`btnCreateInvoice`/`btnForward3` di step akhir) → simpan bisnis, set `isFirstTime=false`, navigasi ke `dashboard_navigation_graph` (popUpTo splash inclusive). Back di step 1 → `navigateUp()` (kembali ke Splash).

### 3. Masukkan PIN (`EnterPasswordFragment`)
**Fitur:** Input PIN 4-digit via `PinLockView`, validasi terhadap PIN tersimpan. PIN benar → masuk Dashboard. PIN salah → tampil pesan error & reset input. Tautan "Lupa Password" untuk alur recovery.
**Widget/Komponen:** `PinLockView` + `IndicatorDots` (library `com.andrognito.pinlockview`), `TextView` pesan error, `TextView` link lupa password.
**User Story:** Sebagai pengguna yang mengaktifkan kunci aplikasi, saya ingin memasukkan PIN saya supaya data bisnis saya tetap aman dari akses tidak sah.
**Navigasi:** Entry: dari Splash (kondisi lock aktif, bukan first-time). Keluar: PIN benar → `dashboard_navigation_graph` (popUpTo splash inclusive). "Lupa Password" → `LockMainFragment` (mode RECOVERY).

### 4. Setup Ulang PIN / Pertanyaan Keamanan (`LockMainFragment`) — *shared dengan Settings*
**Fitur:** Input jawaban keamanan (nickname rahasia) untuk mode NEW (aktivasi awal lock) atau RECOVERY (lupa PIN, harus cocok dengan jawaban tersimpan). Validasi non-kosong dan (untuk recovery) kecocokan jawaban.
**Widget/Komponen:** `EditText` jawaban keamanan (`editRecoverAnswer`), `MaterialButton` simpan (`btnSaveSecurity`). Back-button system di-override untuk `navigateUp()`.
**User Story:** Sebagai pengguna yang mengaktifkan PIN, saya ingin mengatur jawaban keamanan supaya saya bisa memulihkan akses kalau lupa PIN nanti.
**Navigasi:** Entry: dari `EnterPasswordFragment` ("Lupa Password") ATAU dari `SettingFragment` (toggle Lock ON, mode NEW). Keluar: → `SavePasswordFragment` dengan `pin_type` yang sama. Back → `navigateUp()`.

### 5. Simpan PIN Baru (`SavePasswordFragment`) — *shared dengan Settings*
**Fitur:** Input PIN baru 4-digit + konfirmasi, validasi panjang (harus 4 digit) dan kecocokan kedua input. Simpan PIN & aktifkan lock mode.
**Widget/Komponen:** 2 input PIN (`editNewPassword`, `editConfirmPassword`), `MaterialButton` simpan. Snackbar error untuk validasi gagal.
**User Story:** Sebagai pengguna, saya ingin menetapkan PIN 4-digit baru supaya saya bisa mengunci akses ke aplikasi.
**Navigasi:** Entry: dari `LockMainFragment`. Keluar: mode NEW → `SettingFragment` (popUpTo+inclusive, bersih dari back stack); mode RECOVERY → langsung ke `dashboard_navigation_graph` (popUpTo splash inclusive).

---

## B. Dashboard

### 6. Dashboard (`DashboardFragment`)
**Fitur:**
- Ringkasan (Overview): 4 card statistik — Total Invoices, Revenue Paid, Outstanding, Open Estimates — tiap card ada angka + trend text (dinamis dari `DashboardViewModel.summary`, meski persentase trend tampak sebagai string siap-pakai dari ViewModel, bukan dihitung ulang di UI).
- Recent Invoices: list max beberapa invoice terbaru (RecyclerView), tap salah satu → navigasi ke modul Invoice; tombol "View All" → modul Invoice. Empty state kalau belum ada invoice.
- Quick Actions grid: New Invoice, New Estimate, Add Client, Add Item — tap langsung ke modul terkait.
- FAB (kanan-bawah) → buka `QuickActionBottomSheet` (opsi New Invoice / New Estimate).
**Widget/Komponen:** `NestedScrollView` pembungkus, `GridLayout` 2 kolom untuk 4 stat card (`MaterialCardView`), `RecyclerView` (`item_recent_invoice`), `GridLayout` 4 kolom Quick Actions, `FloatingActionButton` (`fabQuickAction`), `BottomSheetDialogFragment` custom (`QuickActionBottomSheet`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat ringkasan kondisi bisnis saya (total invoice, pendapatan, tagihan belum dibayar) begitu buka app, supaya saya cepat tahu apa yang perlu ditindaklanjuti.
**Navigasi:** Entry: start destination `mobile_navigation` (root app setelah lewat Splash), atau tab "Dashboard" di bottom nav. Keluar: ke `invoice_navigation_graph` (Recent Invoices/View All/Quick Action New Invoice), `estimate_navigation_graph`, `client_navigation_graph`, `item_navigation_graph` — semua navigasi lintas-graph (bukan `action` nav-graph biasa, langsung `navigate(R.id.xxx_navigation_graph)`). Back dari sini → keluar app (top-level destination).

---

## C. Invoice (20 halaman)

### 7. Daftar Invoice (`InvoiceMainFragment`)
**Fitur:** 3 tab (All/Outstanding/Paid) dengan hitungan jumlah di label tab, live-update dari `allInvoicesLive`. 3 summary card di atas tab (Total Invoices, Outstanding, Paid) dengan warna kontekstual. Search bar (`editSearch`) yang memfilter via `viewModel.updateSearchQuery()`. FAB tambah invoice baru (`btnAddInvoice`) — langsung insert record kosong ke DB lalu navigasi ke wizard Edit. Back-button di-intercept → dialog custom "Exit App?" (bukan behavior standar).
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapter`, host 3 tab fragment), `view_invoice_summary_card` x3 (custom merge-layout), `EditText` search, `FloatingActionButton`, `Dialog` custom exit (`exit_dialog.xml`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua invoice saya dikelompokkan per status (Semua/Belum Dibayar/Lunas) supaya saya bisa memantau mana yang perlu ditagih.
**Navigasi:** Entry: tab "Invoice" di bottom nav, atau dari Dashboard (Recent Invoices/Quick Action). Keluar: FAB → `AddInvoiceMainFragment` (wizard edit, mode NEW). Tap item invoice (di 3 tab) → wizard edit (mode existing). Back → dialog konfirmasi keluar app.

### 8. Tab "All" (`AllInvoices`)
**Fitur:** List semua invoice (RecyclerView), diurutkan terbaru dulu (`sortedByDescending invoiceId`). Empty state kalau kosong.
**Widget/Komponen:** `RecyclerView` (adapter `AllInvoiceAdapter`), komponen Empty State (`emptyState` merge-include).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat seluruh invoice tanpa filter supaya saya punya gambaran lengkap.
**Navigasi:** Entry: tab pertama di `InvoiceMainFragment`. Keluar: tap item → wizard edit invoice (via adapter, ke `AddInvoiceMainFragment`).

### 9. Tab "Outstanding" (`OutstandingInvoices`)
**Fitur:** List invoice yang difilter status `UN_PAID` saja. Empty state kalau kosong.
**Widget/Komponen:** `RecyclerView` (adapter `AllInvoiceAdapter` di-reuse), `TextView` empty state manual (bukan merge-include seperti tab All).
**User Story:** Sebagai pemilik bisnis, saya ingin fokus melihat invoice yang belum dibayar supaya saya tahu siapa yang perlu ditagih.
**Navigasi:** Sama seperti tab All (tap item → wizard edit).

### 10. Tab "Paid" (`PaidInvoices`)
**Fitur:** List invoice berstatus `PAID` saja. Sama struktur dengan Outstanding.
**Widget/Komponen:** `RecyclerView`, `TextView` empty state.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat invoice yang sudah lunas sebagai catatan pendapatan yang sudah masuk.
**Navigasi:** Sama seperti tab All.

### 11. Wizard Invoice — shell (`AddInvoiceMainFragment`)
**Fitur:** Wrapper dengan 3 tab internal (Edit/Preview/History via ViewPager2). Toolbar: back (simpan otomatis lalu keluar), menu titik-tiga (Delete/Share), tombol Share Invoice (generate PDF via WebView→PDF, lalu share via Android Share Sheet; minta permission storage kalau perlu). Delete invoice via `BaseDialog` konfirmasi.
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapterEditInvoice`), toolbar lama (`imgBack`/`imgThreeDot`), `PopupMenu` (Delete/Share), `WebView` tersembunyi (untuk render HTML→PDF), `Snackbar` (izin storage ditolak).
**User Story:** Sebagai pemilik bisnis, saya ingin mengedit detail invoice, melihat pratinjau hasil akhirnya, dan membagikannya sebagai PDF ke klien saya.
**Navigasi:** Entry: dari `InvoiceMainFragment` (FAB atau tap item). Keluar: back/toolbar-back → simpan seluruh field ViewModel ke DB lalu `navigateUp()` (balik ke list Invoice). Delete → konfirmasi lalu `navigateUp()`.

### 12. Tab "Edit" (`EditInvoiceFragment`)
**Fitur:** Form utama invoice — nomor & tanggal invoice, info bisnis, pilih klien (langsung ke detail kalau sudah pernah pilih, atau ke daftar klien kalau belum), daftar item (tambah/lihat), signature, foto lampiran, diskon, pajak, catatan, instruksi pembayaran, toggle status Mark Paid/Unpaid, link ke daftar pembayaran.
**Widget/Komponen:** Banyak `TextView` yang berfungsi sebagai "field" clickable (bukan EditText, kecuali Notes & Payment Instruction yang sudah jadi `TextInputLayout`), `RecyclerView` item invoice (`SelectedInvoiceItemsAdapter`) & foto (`SelectedPhotosForInvoiceAdapter`), `MaterialButton` Mark Paid/Unpaid.
**User Story:** Sebagai pemilik bisnis, saya ingin mengisi semua detail invoice (klien, item, diskon, pajak) dalam satu layar terpusat supaya proses pembuatan invoice cepat.
**Navigasi:** Entry: tab pertama wizard Invoice. Keluar (semua via tap field, kembali ke sini setelah selesai): Tax, Signature, Add Photo, Invoice Information (nomor/tanggal/PO/terms), Business Detail, Client List/Detail, Item Detail, Discount, Payments.

### 13. Tab "Preview" (`PreviewInvoiceFragment`)
**Fitur:** Render pratinjau invoice sebagai halaman HTML lengkap (logo, info klien, tabel item, subtotal/pajak/diskon/total, instruksi pembayaran, foto lampiran, tanda tangan, info kontak bisnis) di dalam `WebView` — inilah yang jadi sumber PDF saat "Share".
**Widget/Komponen:** `WebView` (satu-satunya komponen utama).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat persis seperti apa invoice akan terlihat sebelum dikirim ke klien.
**Navigasi:** Entry: tab kedua wizard Invoice (tidak ada navigasi keluar dari sini selain ganti tab).

### 14. Tab "History" (`InvoiceShareHistoryFragment`)
**Fitur:** Saat ini kosong — layout ter-inflate tapi tidak ada logic/data yang dimuat (belum diimplementasikan sepenuhnya).
**Widget/Komponen:** Layout placeholder (`fragment_invoice_share_history`), tidak ada widget aktif dengan data.
**User Story:** *(belum terpenuhi)* Sebagai pemilik bisnis, saya ingin melihat riwayat kapan/ke mana invoice ini pernah dibagikan.
**Navigasi:** Entry: tab ketiga wizard Invoice. Tidak ada aksi navigasi keluar dari dalam halaman ini.

### 15. Tanda Tangan (`SignatureFragment`)
**Fitur:** Kanvas gambar tanda tangan (`SignatureView`), tombol Clear, OK (simpan ke ViewModel sementara) dan Cancel (batal).
**Widget/Komponen:** `SignatureView` custom, `MaterialButton` Clear/OK/Cancel, toolbar dengan judul "Signature".
**User Story:** Sebagai pemilik bisnis, saya ingin menandatangani invoice secara digital supaya terlihat lebih resmi.
**Navigasi:** Entry: dari tab Edit (txtSignature). Keluar: OK/Cancel/toolbar-back → `navigateUp()` kembali ke tab Edit.

### 16. Tambah Foto (`AddPhotoToInvoiceFragment`)
**Fitur:** Pilih/ambil foto (kamera atau galeri via bottom sheet), isi deskripsi & detail tambahan foto, simpan (insert/update) atau hapus (kalau foto lama, via konfirmasi `BaseDialog`). Validasi wajib pilih foto dulu sebelum simpan.
**Widget/Komponen:** `ImageView` foto (`imgPhoto`, klik untuk pilih ulang), `BottomSheetDialog` (Camera/Gallery), `EditText` deskripsi & detail, toolbar dengan ikon delete (kondisional, hanya untuk foto lama).
**User Story:** Sebagai pemilik bisnis, saya ingin melampirkan foto (misal bukti kerja/barang) ke invoice supaya klien punya konteks visual.
**Navigasi:** Entry: dari tab Edit (txtAddPhoto) — argumen `photo_type` NEW/OLD. Keluar: back/toolbar-back → simpan otomatis → `navigateUp()`. Delete → konfirmasi → hapus → `navigateUp()`.

### 17. Info Invoice (`InvoiceInformationFragment`)
**Fitur:** Edit nomor invoice, tanggal invoice (date picker), tanggal jatuh tempo (date picker), PO number, dan Terms (bottom sheet pilihan: None/Due on Receipt/Next Day/2 Days/1 Week/Custom). Validasi nomor invoice tidak boleh kosong.
**Widget/Komponen:** `EditText` nomor/PO, `TextView` clickable untuk tanggal (buka `DatePickerDialog` via `DialogManager`) dan terms (buka `BottomSheetDialog` custom `bottom_sheet_terms`).
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur nomor invoice, tanggal, dan syarat pembayaran supaya invoice sesuai kebutuhan administrasi saya.
**Navigasi:** Entry: dari tab Edit (txtInvoiceDate/txtInvoiceNumber/txtDueOnReceipt). Keluar: back/toolbar-back → simpan → `navigateUp()`.

### 18. Detail Bisnis dari Invoice (`EditBusinessDetailsFromInvoiceFragment`)
**Fitur:** Edit profil bisnis (nama, pemilik, nomor bisnis, alamat, email, telepon, website, logo) langsung dari konteks invoice — perubahan di sini memengaruhi profil bisnis global (`viewModel.updateBusiness()`).
**Widget/Komponen:** `ImageView` logo (klik → bottom sheet Camera/Gallery), banyak `EditText`.
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui info bisnis saya (misal alamat baru) langsung dari layar invoice tanpa harus ke Settings.
**Navigasi:** Entry: dari tab Edit (txtBusinessInfo). Keluar: back/toolbar-back → simpan → `navigateUp()`.

### 19. Daftar Klien untuk Invoice (`ClientListForInvoiceFragment`)
**Fitur:** List semua klien (dari cache ViewModel, bukan query ulang DB) untuk dipilih sebagai klien invoice ini.
**Widget/Komponen:** `RecyclerView` (adapter `AllClientsForInvoiceAdapter`), toolbar dengan judul "Clients".
**User Story:** Sebagai pemilik bisnis, saya ingin memilih klien yang sudah ada dari daftar supaya tidak perlu mengetik ulang datanya.
**Navigasi:** Entry: dari tab Edit (secondCard, kalau belum ada klien terpilih & daftar klien tidak kosong). Keluar: tap klien → set `selectedClient` → `navigateUp()` (via adapter). Toolbar-back → `navigateUp()` tanpa pilih.

### 20. Detail Klien untuk Invoice (`ClientDetailForInvoiceFragment`)
**Fitur:** Form detail klien (nama, email, mobile/telepon/fax, kontak, 3 baris alamat) — dipakai baik untuk melihat/edit klien yang sudah dipilih, maupun input klien baru ad-hoc (tanpa disimpan ke tabel Client permanen — `clientId` di-set ke `Int.MAX_VALUE` sebagai penanda sementara). Tombol Delete (icon di toolbar) untuk membatalkan pilihan klien.
**Widget/Komponen:** Banyak `EditText`, toolbar dengan `imgDelete` selalu visible.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat/menyesuaikan detail klien yang dipilih untuk invoice ini, atau memasukkan klien baru langsung di tempat.
**Navigasi:** Entry: dari tab Edit (secondCard, kalau sudah ada klien terpilih ATAU daftar klien kosong). Keluar: back/toolbar-back → validasi nama wajib diisi → simpan ke `viewModel.selectedClient` → `navigateUp()`. Delete → clear `selectedClient` → `navigateUp()`.

### 21. Detail Item untuk Invoice (`ItemDetailForInvoiceFragment`)
**Fitur:** Form item invoice — deskripsi, harga satuan, kuantitas (kalkulasi total real-time via `ItemCalculator`), jenis diskon (bottom sheet: Percentage/Flat Amount), jumlah diskon, toggle kena-pajak (+field tax rate kalau aktif), detail tambahan. Icon search di toolbar (hanya utk item NEW) → ke daftar item existing. Delete (khusus item lama) via `BaseDialog`.
**Widget/Komponen:** `TextInputLayout` untuk Description & Additional Details (field lain masih label-beside-value biasa), `Switch` kena-pajak, toolbar dengan `imgSearch`/`imgDelete` kondisional berdasar NEW/OLD.
**User Story:** Sebagai pemilik bisnis, saya ingin menambahkan item ke invoice dengan harga, jumlah, dan diskon supaya totalnya terhitung otomatis.
**Navigasi:** Entry: dari tab Edit (txtAddItem, item baru) atau dari list item invoice existing (edit). Keluar: back/toolbar-back → validasi nama item wajib → hitung & simpan → `navigateUp()`. Search icon → `ItemsListForInvoice`. Delete → konfirmasi → hapus → `navigateUp()`.

### 22. Daftar Item Tersimpan untuk Invoice (`ItemsListForInvoice`)
**Fitur:** List semua item dari master data Item (bukan item invoice), untuk dipilih cepat mengisi form Item Detail.
**Widget/Komponen:** `RecyclerView` (adapter `AllItemsForInvoiceAdapter`), empty state (`txtEmptyItemList`).
**User Story:** Sebagai pemilik bisnis, saya ingin memilih item dari daftar item master saya supaya tidak perlu mengetik ulang nama & harga tiap kali buat invoice.
**Navigasi:** Entry: dari `ItemDetailForInvoiceFragment` (icon search). Keluar: tap item → isi form Item Detail (via adapter) → `navigateUp()`. Toolbar-back → `navigateUp()`.

### 23. Diskon Invoice (`DiscountFragment`)
**Fitur:** Pilih jenis diskon keseluruhan invoice (bottom sheet: No Discount/Percentage/Flat Amount) + jumlah diskon, hitung total diskon via `DiscountCalculator` berdasar subtotal invoice.
**Widget/Komponen:** `TextView` jenis diskon (clickable, buka `bottom_sheet_discount`), `EditText`/group jumlah diskon (visible/hidden sesuai jenis).
**User Story:** Sebagai pemilik bisnis, saya ingin memberi diskon keseluruhan pada invoice (persentase atau nominal tetap) untuk klien tertentu.
**Navigasi:** Entry: dari tab Edit (txtDiscountPrice). Keluar: back/toolbar-back → hitung & simpan → `navigateUp()`.

### 24. Pajak Invoice (`TaxFragment`)
**Fitur:** Pilih jenis pajak (bottom sheet: None/On The Total/Deducted/Per Item), label pajak custom, tarif pajak, toggle "tax inclusive". Tampilan field berubah (show/hide group) sesuai jenis pajak dipilih.
**Widget/Komponen:** `TextView` jenis pajak (clickable), `EditText` label & rate, `Switch` inclusive, beberapa `View`/`group` yang toggle visibility (`viewLabel`/`viewRate`/`secondCard`).
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur pajak yang berlaku pada invoice sesuai kebijakan pajak lokal saya.
**Navigasi:** Entry: dari tab Edit (txtTaxPrice). Keluar: back/toolbar-back → simpan → `navigateUp()`.

### 25. Daftar Pembayaran (`InvoicePaymentsListFragment`)
**Fitur:** List pembayaran yang sudah dicatat untuk invoice ini, total dibayar & sisa saldo (balance due after payment) dihitung otomatis.
**Widget/Komponen:** `RecyclerView` (adapter `PaymentsAdapter`), `TextView` "Add Payment" (link tambah baru).
**User Story:** Sebagai pemilik bisnis, saya ingin mencatat pembayaran cicilan/parsial yang diterima untuk invoice ini dan melihat sisa tagihannya.
**Navigasi:** Entry: dari tab Edit (txtPayment/txtPaymentPrice). Keluar: "Add Payment" → `InvoiceAddPaymentFragment`. Toolbar-back → `navigateUp()`.

### 26. Tambah Pembayaran (`InvoiceAddPaymentFragment`)
**Fitur:** Input jumlah pembayaran, tanggal (default hari ini, bisa diubah via date picker), metode pembayaran (bottom sheet: Cash/Cheque/Bank/Credit Card/PayPal/Other), catatan. Validasi jumlah wajib diisi.
**Widget/Komponen:** `EditText` jumlah & catatan, `TextView` tanggal & metode (clickable, masing-masing buka dialog/bottom sheet berbeda).
**User Story:** Sebagai pemilik bisnis, saya ingin mencatat detail satu transaksi pembayaran (jumlah, tanggal, metode) yang diterima dari klien.
**Navigasi:** Entry: dari `InvoicePaymentsListFragment` ("Add Payment"). Keluar: back/toolbar-back → validasi & simpan ke list pembayaran → `navigateUp()`.

---

## D. Estimate (18 halaman — mirror pola Invoice)

> Estimate secara struktural adalah duplikasi 1:1 dari Invoice, dengan perbedaan: **tidak ada** konsep due-date/terms/payment-tracking (karena estimasi bukan tagihan), status hanya Open/Closed (bukan Paid/Unpaid), dan module ini **belum** mendapat redesign visual (summary card/search bar) seperti `InvoiceMainFragment` — masih pakai toolbar lama sepenuhnya + `imgSearch.visible()` yang sebetulnya tidak difungsikan jadi search box nyata (tidak ada `editSearch` di layout).

### 27. Daftar Estimate (`EstimatesMainFragment`)
**Fitur:** 3 tab (All/Open/Closed) dengan hitungan jumlah di label. Ringkasan jumlah (total/open/closed) di beberapa `TextView`. FAB tambah estimate baru — insert record kosong lalu ke wizard edit.
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapterEstimates`), `TextView` ringkasan (`txtTotalCount`/`txtOpenCount`/`txtClosedCount`), FAB (`btnAddEstimate`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua estimasi/penawaran harga yang pernah saya buat, dikelompokkan status terbuka/tertutup.
**Navigasi:** Entry: tab "Estimate" bottom nav, atau dari Dashboard Quick Action. Keluar: FAB → `AddEstimateMainFragment` (wizard, mode baru). Tap item → wizard edit (mode existing). Tidak ada override back-button khusus (beda dari Invoice).

### 28. Tab "All" (`AllEstimatesFragment`)
**Fitur:** List semua estimate, urut terbaru dulu. Empty state (merge-include).
**Widget/Komponen:** `RecyclerView` (adapter `AllEstimateAdapter`), `emptyState` merge-include.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat seluruh estimasi tanpa filter status.
**Navigasi:** Tab pertama `EstimatesMainFragment`. Tap item → wizard edit estimate.

### 29. Tab "Open" (`OpenEstimatesFragment`)
**Fitur:** List estimate berstatus `OPEN` saja.
**Widget/Komponen:** `RecyclerView`, `TextView` empty state (`txtEmptyEstimatesList`).
**User Story:** Sebagai pemilik bisnis, saya ingin fokus ke estimasi yang masih menunggu keputusan klien.
**Navigasi:** Sama seperti tab All.

### 30. Tab "Closed" (`ClosedEstimatesFragment`)
**Fitur:** List estimate berstatus `CLOSED` saja.
**Widget/Komponen:** `RecyclerView`, `TextView` empty state.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat riwayat estimasi yang sudah selesai diproses (diterima/ditolak/kadaluarsa).
**Navigasi:** Sama seperti tab All.

### 31. Wizard Estimate — shell (`AddEstimateMainFragment`)
**Fitur:** Sama seperti `AddInvoiceMainFragment` — 3 tab (Edit/Preview/History), toolbar back (auto-save), menu Delete/Share PDF.
**Widget/Komponen:** `TabLayout`+`ViewPager2` (`ViewPagerAdapterEditEstimate`), `WebView` tersembunyi, `PopupMenu`.
**User Story:** Sebagai pemilik bisnis, saya ingin menyusun estimasi harga untuk calon klien dan membagikannya sebagai PDF.
**Navigasi:** Entry: dari `EstimatesMainFragment`. Keluar: back → simpan semua field → `navigateUp()`. Delete → konfirmasi → `navigateUp()`.

### 32. Tab "Edit" (`EditEstimateFragment`)
**Fitur:** Sama seperti Invoice Edit tapi tanpa Payment/Terms/Due-Date — nomor & tanggal estimasi, info bisnis, klien, item, signature, foto, diskon, pajak, catatan, toggle status Mark Open/Closed.
**Widget/Komponen:** Struktur identik `EditInvoiceFragment` minus bagian pembayaran.
**User Story:** Sebagai pemilik bisnis, saya ingin mengisi detail estimasi (klien, item, harga) dalam satu layar.
**Navigasi:** Sama pola seperti Invoice Edit, ke: Signature, Info Estimate, Business Detail, Client List/Detail, Item Detail, Discount, Tax, Add Photo (semua versi Estimate).

### 33. Tab "Preview" (`PreviewEstimateFragment`)
**Fitur:** Render HTML pratinjau estimasi (sama strukturnya dengan Invoice Preview, tanpa bagian pembayaran).
**Widget/Komponen:** `WebView`.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat tampilan akhir estimasi sebelum dikirim ke calon klien.
**Navigasi:** Tab kedua wizard Estimate.

### 34. Tab "History" (`EstimateShareHistoryFragment`)
**Fitur:** Sama seperti Invoice History — kosong/belum diimplementasikan.
**Widget/Komponen:** Layout placeholder.
**User Story:** *(belum terpenuhi)* Melihat riwayat pengiriman estimasi.
**Navigasi:** Tab ketiga wizard Estimate.

### 35. Tanda Tangan Estimate (`EstimateSignatureFragment`)
**Fitur & Widget/Komponen:** Identik `SignatureFragment` Invoice.
**User Story:** Sebagai pemilik bisnis, saya ingin menandatangani estimasi secara digital.
**Navigasi:** Entry dari tab Edit Estimate (txtSignature). Keluar: OK/Cancel → `navigateUp()`.

### 36. Info Estimate (`EstimateInformationFragment`)
**Fitur:** Edit nomor estimasi, tanggal (date picker), PO number. **Tidak ada** field due-date/terms (beda dari Invoice Information).
**Widget/Komponen:** `EditText` nomor/PO, `TextView` tanggal clickable.
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur nomor dan tanggal estimasi.
**Navigasi:** Entry dari tab Edit (txtEstimateDate/txtEstimateNumber). Keluar: validasi nomor wajib → simpan → `navigateUp()`.

### 37. Detail Bisnis dari Estimate (`EditBusinessDetailsFromEstimateFragment`)
**Fitur & Widget/Komponen:** Identik `EditBusinessDetailsFromInvoiceFragment`.
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui profil bisnis dari konteks estimasi.
**Navigasi:** Entry dari tab Edit (txtBusinessInfo). Keluar: simpan → `navigateUp()`.

### 38. Daftar Klien untuk Estimate (`ClientListForEstimateFragment`)
**Fitur & Widget/Komponen:** Identik versi Invoice, adapter `AllClientsForEstimateAdapter`.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih klien dari daftar untuk estimasi ini.
**Navigasi:** Entry dari tab Edit (secondCard). Keluar: pilih klien → `navigateUp()`.

### 39. Detail Klien untuk Estimate (`ClientDetailForEstimateFragment`)
**Fitur & Widget/Komponen:** Identik versi Invoice (form klien ad-hoc + delete pilihan).
**User Story:** Sebagai pemilik bisnis, saya ingin menyesuaikan/menghapus data klien pada estimasi ini.
**Navigasi:** Entry dari tab Edit (secondCard, kondisi klien sudah dipilih/list kosong). Keluar: simpan/hapus → `navigateUp()`.

### 40. Detail Item untuk Estimate (`ItemDetailForEstimateFragment`)
**Fitur & Widget/Komponen:** Identik `ItemDetailForInvoiceFragment` (kalkulasi via `ItemCalculator`, bottom sheet jenis diskon, toggle pajak).
**User Story:** Sebagai pemilik bisnis, saya ingin menambahkan item dengan harga & diskon ke estimasi.
**Navigasi:** Entry dari tab Edit (txtAddItem) atau list item existing. Keluar: simpan/hapus → `navigateUp()`. Ke `ItemListForEstimate` (kalau ada tombol search — perlu verifikasi lanjut, layout serupa Invoice).

### 41. Daftar Item Tersimpan untuk Estimate (`ItemListForEstimate`)
**Fitur & Widget/Komponen:** Identik `ItemsListForInvoice`, adapter `AllItemsForEstimateAdapter`.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih item dari master data untuk estimasi ini.
**Navigasi:** Entry dari Item Detail Estimate. Keluar: pilih item → `navigateUp()`.

### 42. Diskon Estimate (`EstimateDiscountFragment`)
**Fitur & Widget/Komponen:** Identik `DiscountFragment` Invoice (kalkulasi berbasis `estimateSubTotal`).
**User Story:** Sebagai pemilik bisnis, saya ingin memberi diskon keseluruhan pada estimasi.
**Navigasi:** Entry dari tab Edit (txtDiscountPrice). Keluar: simpan → `navigateUp()`.

### 43. Pajak Estimate (`EstimateTaxFragment`)
**Fitur & Widget/Komponen:** Identik `TaxFragment` Invoice.
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur pajak yang berlaku pada estimasi.
**Navigasi:** Entry dari tab Edit (txtTaxPrice). Keluar: simpan → `navigateUp()`.

### 44. Tambah Foto Estimate (`AddPhotoToEstimateFragment`)
**Fitur & Widget/Komponen:** Identik `AddPhotoToInvoiceFragment`.
**User Story:** Sebagai pemilik bisnis, saya ingin melampirkan foto ke estimasi.
**Navigasi:** Entry dari tab Edit (txtAddPhoto). Keluar: simpan/hapus → `navigateUp()`.

---

## E. Client

### 45. Daftar Klien (`ClientMainFragment`)
**Fitur:** List semua klien (RecyclerView), empty state kalau kosong. Tombol tambah klien baru.
**Widget/Komponen:** `RecyclerView` (adapter `AllClientsAdapter`), `emptyState` merge-include, `FloatingActionButton`/tombol `btnAddClient`.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua klien saya dalam satu daftar supaya mudah memilihnya saat buat invoice/estimasi.
**Navigasi:** Entry: dari "More" (menuClients) atau Dashboard Quick Action (Add Client, langsung ke sini bukan ke form tambah). Keluar: FAB → `AddClient` (mode NEW). Tap item klien (via adapter) → `AddClient` (mode EXISTING, edit).

### 46. Tambah/Edit Klien (`AddClient`)
**Fitur:** Form klien (nama, email, kontak, telepon/mobile/fax, 3 baris alamat). Auto-save saat back (kalau nama diisi). Menu titik-tiga (khusus edit klien existing) → Delete via `BaseDialog`.
**Widget/Komponen:** Banyak `EditText`, `PopupMenu` (menu delete), toolbar dengan `imgThreeDot` kondisional.
**User Story:** Sebagai pemilik bisnis, saya ingin menambah klien baru atau memperbarui data klien yang sudah ada.
**Navigasi:** Entry: dari `ClientMainFragment` (FAB atau tap item). Keluar: back/toolbar-back → simpan (kalau nama tidak kosong) → `navigateUp()`. Delete → konfirmasi → hapus → `navigateUp()`.

---

## F. Item

### 47. Daftar Item (`ItemMainFragment`)
**Fitur:** List semua item master (RecyclerView), empty state kalau kosong. Tombol tambah item baru.
**Widget/Komponen:** `RecyclerView` (adapter `AllItemsAdapter`), `emptyState` merge-include, tombol `btnAddItem`.
**User Story:** Sebagai pemilik bisnis, saya ingin mengelola katalog item/jasa yang biasa saya jual supaya cepat dipilih saat membuat invoice.
**Navigasi:** Entry: dari "More" (menuItems) atau Dashboard Quick Action (Add Item). Keluar: tombol tambah → `AddItemFragment` (NEW). Tap item → `AddItemFragment` (EXISTING).

### 48. Tambah/Edit Item (`AddItemFragment`)
**Fitur:** Form item (deskripsi, harga satuan, toggle kena-pajak, detail tambahan). Auto-save saat back. Menu titik-tiga (khusus edit) → Delete via `BaseDialog`.
**Widget/Komponen:** `EditText` deskripsi/harga/detail, `Switch` kena-pajak, `PopupMenu` delete.
**User Story:** Sebagai pemilik bisnis, saya ingin menambah atau memperbarui item di katalog saya (harga, status kena pajak).
**Navigasi:** Entry: dari `ItemMainFragment`. Keluar: back/toolbar-back → simpan → `navigateUp()`. Delete → konfirmasi → hapus → `navigateUp()`.

---

## G. Reports

### 49. Shell Reports (`ReportsMainFragment`)
**Fitur:** 2 tab (Paid/Clients) via ViewPager2+TabLayout dengan ikon per tab.
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapterReport`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat laporan ringkas soal invoice yang lunas dan performa per klien.
**Navigasi:** Entry: tab "Reports" bottom nav. Tidak ada navigasi keluar dari shell ini selain ganti tab.

### 50. Tab "Paid" (`PaidReportFragment`)
**Fitur:** List semua invoice lunas + kartu ringkasan (total invoice lunas, total nilai dibayar, rata-rata per invoice, jumlah klien unik) dihitung dari data live.
**Widget/Komponen:** `RecyclerView` (adapter `PaidReportAdapter`), beberapa `TextView` ringkasan (`txtTotalInvoices`/`txtTotalPaid`/`txtAvgInvoice`/`txtTotalClients`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat total pendapatan yang sudah masuk dan performa rata-rata invoice saya.
**Navigasi:** Tab pertama Reports, tidak ada navigasi keluar.

### 51. Tab "Clients" (`ClientsReportFragment`)
**Fitur:** List ringkasan performa per klien (total tagihan per klien, dari `viewModel.getClientReport()`).
**Widget/Komponen:** `RecyclerView` (adapter `ClientsAdapter`).
**User Story:** Sebagai pemilik bisnis, saya ingin tahu klien mana yang paling banyak berkontribusi ke pendapatan saya.
**Navigasi:** Tab kedua Reports, tidak ada navigasi keluar.

---

## H. Settings

### 52. Pengaturan (`SettingFragment`)
**Fitur:** Link ke Profil Bisnis. Toggle Lock (aktifkan → ke setup PIN baru; nonaktifkan → langsung matikan lock mode). Toggle Dark Mode (langsung terapkan tema via `AppCompatDelegate`, simpan preferensi).
**Widget/Komponen:** `TextView` "Personal Business" (clickable), `Switch` Lock & Dark Mode, toolbar dengan tombol back.
**User Story:** Sebagai pengguna, saya ingin mengatur preferensi aplikasi (keamanan PIN, tema gelap/terang) dan mengakses profil bisnis saya.
**Navigasi:** Entry: dari ikon Settings di header (semua layar top-level) atau dari "More" (menuSettings). Keluar: "Personal Business" → `AddBusinessDetailsFragment`. Toggle Lock ON → `LockMainFragment` (mode NEW). Toolbar-back → `navigateUp()`.

### 53. Profil Bisnis (`AddBusinessDetailsFragment`)
**Fitur:** Sama seperti setup bisnis awal (nama, alamat, kontak, email, telepon, website, logo) tapi dalam konteks edit dari Settings, bukan onboarding.
**Widget/Komponen:** `ImageView` logo (bottom sheet Camera/Gallery), banyak `EditText`.
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui profil bisnis saya kapan saja lewat menu Pengaturan.
**Navigasi:** Entry: dari `SettingFragment`. Keluar: back → simpan → `navigateUp()`.

---

## I. More

### 54. Menu Lainnya (`MoreFragment`)
**Fitur:** 11 menu item dikelompokkan (Business: Clients, Items; Tools: Backup & Restore, Export Data, PDF Templates, Settings; Support: Help Center, Feedback, Rate App, About; Logout). **Hanya 3 yang benar-benar berfungsi** (Clients, Items, Settings) — 8 sisanya (Backup & Restore, Export Data, PDF Templates, Help Center, Feedback, Rate App, About, Logout) tampil visual lengkap (ikon+judul+subtitle) TAPI **tidak ada click listener terpasang** sama sekali, jadi tap tidak melakukan apa-apa.
**Widget/Komponen:** 11 `view_more_menu_item` (custom merge-layout: `MaterialCardView` ikon + judul + subtitle), dikelompokkan visual per section.
**User Story:** Sebagai pengguna, saya ingin mengakses fitur-fitur sekunder (kelola klien/item, pengaturan, bantuan) dari satu menu terpusat.
**Navigasi:** Entry: tab "More" bottom nav. Keluar (yang berfungsi): Clients → `client_navigation_graph`. Items → `item_navigation_graph`. Settings → `setting_navigation_graph`. 8 menu lain: tidak ada navigasi (dead UI).
