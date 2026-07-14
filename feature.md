# Koinvois — Fitur per Alur Pengguna (User Journey)

Revisi: 2026-07-13. Dokumen ini adalah revisi dari `feature.md` versi lama (sudah usang) dan `PAGES.md` (akurat per kode terkini, dikelompokkan per modul teknis). Isi 54 halaman di sini SAMA dengan `PAGES.md` — sumber kebenaran teknisnya tetap `PAGES.md` — tapi dikelompokkan ulang berdasarkan **alur pemakaian nyata (user journey)**, bukan struktur modul/package Android. Untuk alur panjang (Invoice, Estimate), halaman diurutkan sesuai langkah yang benar-benar dilalui user membuat satu invoice/estimasi dari awal sampai selesai, dan tiap sub-halaman wizard diberi penanda arah "← dari" / "→ lanjut ke" supaya bisa dibaca sebagai cerita linear, bukan daftar terpisah-pisah.

Tiap alur (## Alur 1–9) diawali blok **"Cerita alur"** — narasi prosa yang menceritakan perjalanan pengguna dari awal sampai akhir alur tersebut sebelum masuk ke rincian teknis per halaman. Baca bagian ini dulu untuk gambaran cepat, lalu rujuk kartu per-halaman di bawahnya untuk detail Fitur/Widget/User Story/Navigasi.

Nomor halaman (#1–#54) dipertahankan sama seperti `PAGES.md` supaya rujukan silang antar-alur tetap konsisten. **Penomoran sudah diverifikasi ulang secara otomatis: 1 sampai 54, berurutan, tanpa duplikat atau loncat.**

## Daftar Isi

- [Ringkasan 9 Alur](#h-ringkasan)
- [Pengelompokan Fitur: Bersama vs Khusus (Invoice ↔ Estimate)](#h-fitur-bersama)
- **[Alur 1: Onboarding & Keamanan Awal](#h-alur-1)** — Halaman #1–#5
  - [1. Splash / Onboarding](#h-1)
  - [2. Setup Profil Bisnis Awal](#h-2)
  - [3. Masukkan PIN](#h-3)
  - [4. Setup Ulang PIN / Pertanyaan Keamanan](#h-4)
  - [5. Simpan PIN Baru](#h-5)
- **[Alur 2: Dashboard / Ringkasan Utama](#h-alur-2)** — Halaman #6
  - [6. Dashboard](#h-6)
- **[Alur 3: Invoice End-to-End](#h-alur-3)** — Halaman #7–#26
  - [7. Daftar Invoice](#h-7)
  - [8. Tab "All"](#h-8)
  - [9. Tab "Outstanding"](#h-9)
  - [10. Tab "Paid"](#h-10)
  - [11. Wizard Invoice — shell](#h-11)
  - [12. Tab "Edit" — HUB WIZARD](#h-12)
  - [13. Tab "Preview"](#h-13)
  - [14. Tab "History"](#h-14)
  - [15. Tanda Tangan](#h-15)
  - [16. Tambah Foto](#h-16)
  - [17. Info Invoice](#h-17)
  - [18. Detail Bisnis dari Invoice](#h-18)
  - [19. Daftar Klien untuk Invoice](#h-19)
  - [20. Detail Klien untuk Invoice](#h-20)
  - [21. Detail Item untuk Invoice](#h-21)
  - [22. Daftar Item Tersimpan untuk Invoice](#h-22)
  - [23. Diskon Invoice](#h-23)
  - [24. Pajak Invoice](#h-24)
  - [25. Daftar Pembayaran](#h-25)
  - [26. Tambah Pembayaran](#h-26)
- **[Alur 4: Estimate End-to-End](#h-alur-4)** — Halaman #27–#44
  - [27. Daftar Estimate](#h-27)
  - [28. Tab "All"](#h-28)
  - [29. Tab "Open"](#h-29)
  - [30. Tab "Closed"](#h-30)
  - [31. Wizard Estimate — shell](#h-31)
  - [32. Tab "Edit" — HUB WIZARD ESTIMATE](#h-32)
  - [33. Tab "Preview"](#h-33)
  - [34. Tab "History"](#h-34)
  - [35. Tanda Tangan Estimate](#h-35)
  - [36. Info Estimate](#h-36)
  - [37. Detail Bisnis dari Estimate](#h-37)
  - [38. Daftar Klien untuk Estimate](#h-38)
  - [39. Detail Klien untuk Estimate](#h-39)
  - [40. Detail Item untuk Estimate](#h-40)
  - [41. Daftar Item Tersimpan untuk Estimate](#h-41)
  - [42. Diskon Estimate](#h-42)
  - [43. Pajak Estimate](#h-43)
  - [44. Tambah Foto Estimate](#h-44)
- **[Alur 5: Manajemen Klien](#h-alur-5)** — Halaman #45–#46
  - [45. Daftar Klien](#h-45)
  - [46. Tambah/Edit Klien](#h-46)
- **[Alur 6: Manajemen Item/Barang](#h-alur-6)** — Halaman #47–#48
  - [47. Daftar Item](#h-47)
  - [48. Tambah/Edit Item](#h-48)
- **[Alur 7: Laporan (Reports)](#h-alur-7)** — Halaman #49–#51
  - [49. Shell Reports](#h-49)
  - [50. Tab "Paid"](#h-50)
  - [51. Tab "Clients"](#h-51)
- **[Alur 8: Pengaturan & Profil Bisnis](#h-alur-8)** — Halaman #52–#53
  - [52. Pengaturan](#h-52)
  - [53. Profil Bisnis](#h-53)
- **[Alur 9: Navigasi "More"](#h-alur-9)** — Halaman #54
  - [54. Menu Lainnya](#h-54)
- [Catatan & Temuan](#h-catatan)

---

<a id="h-ringkasan"></a>
## Ringkasan 9 Alur

1. **[Onboarding & Keamanan Awal](#h-alur-1)** (Halaman #1–#5) — sekali dilalui saat pertama install: splash → isi profil bisnis → (opsional) atur PIN.
2. **[Dashboard / Ringkasan Utama](#h-alur-2)** (Halaman #6) — titik masuk harian setelah onboarding selesai.
3. **[Invoice End-to-End](#h-alur-3)** (Halaman #7–#26) — dari daftar invoice sampai wizard lengkap (isi data → preview → share PDF).
4. **[Estimate End-to-End](#h-alur-4)** (Halaman #27–#44) — mirror Invoice, untuk penawaran harga sebelum jadi invoice resmi.
5. **[Manajemen Klien](#h-alur-5)** (Halaman #45–#46) — kelola daftar klien yang dipakai ulang di Invoice/Estimate.
6. **[Manajemen Item/Barang](#h-alur-6)** (Halaman #47–#48) — kelola katalog item/jasa yang dipakai ulang di Invoice/Estimate.
7. **[Laporan (Reports)](#h-alur-7)** (Halaman #49–#51) — ringkasan pendapatan & performa klien.
8. **[Pengaturan & Profil Bisnis](#h-alur-8)** (Halaman #52–#53) — preferensi app, keamanan, dan identitas bisnis (di luar konteks onboarding).
9. **[Navigasi "More"](#h-alur-9)** (Halaman #54) — menu akses cepat ke fitur sekunder.

---

<a id="h-fitur-bersama"></a>
## Pengelompokan Fitur: Bersama (Invoice ↔ Estimate) vs Khusus Per-Modul

Di luar pengelompokan per-alur di atas, ini sudut pandang KEDUA: mengelompokkan berdasarkan **fitur/kapabilitas** yang benar-benar dipakai bersama antara Invoice dan Estimate (struktur & layout identik) versus yang hanya ada di satu modul saja. Berguna untuk memahami kenapa kedua modul terasa "kembar" (lihat juga hasil audit konsolidasi layout di `UI_SPRINTS.md`).

<a id="h-fitur-bersama-dipakai-identik-oleh-invoice-estimate"></a>
### Fitur Bersama (dipakai IDENTIK oleh Invoice & Estimate)

| Fitur | Halaman Invoice | Halaman Estimate | Status layout |
|---|---|---|---|
| Tanda Tangan Digital | #15 `SignatureFragment` | #35 `EstimateSignatureFragment` | ✅ **Sudah 1 file** — `fragment_signature.xml` dipakai literal oleh keduanya |
| Info Dokumen (nomor & tanggal) | #17 | #36 | Layout identik, masih 2 file terpisah (`fragment_invoice_information.xml` vs `fragment_estimate_information.xml`) — **beda isi field asli** (Invoice ada Due Date/Terms, Estimate tidak), jadi TIDAK digabung |
| Info Bisnis (kop dokumen) | #18 | #37 | Layout identik (beda `tools:context` saja) — kandidat gabung |
| Pemilihan Klien (list + detail) | #19–#20 | #38–#39 | List: layout identik (kandidat gabung). Detail: **sudah beda** — Invoice sudah pakai komponen `KoinvoisLabel` baru, Estimate masih pola lama |
| Pemilihan Item (form + list) | #21–#22 | #40–#41 | Layout identik (kandidat gabung) |
| Diskon | #23 | #42 | Layout identik, 1 string resource key beda — kandidat gabung |
| Pajak | #24 | #43 | Layout identik, beberapa string resource key beda — kandidat gabung |
| Lampiran Foto | #16 | #44 | Layout identik (beda `tools:context` saja) — kandidat gabung |
| Preview Dokumen | #13 `PreviewInvoiceFragment` | #33 `PreviewEstimateFragment` | Sama-sama render `WebView` HTML, tanpa bagian pembayaran di versi Estimate |
| Riwayat Berbagi (History tab) | #14 | #34 | Sama-sama placeholder kosong — belum diimplementasikan di keduanya |
| Wizard shell (Edit/Preview/History tab) | #11 | #31 | Struktur `TabLayout`+`ViewPager2` identik |
| Daftar dokumen (list utama, search/sort/filter/stat card) | #7 | #27 | Sudah sama-sama dapat UI modern (search bar, filter, kartu ringkasan) |

<a id="h-fitur-khusus-invoice-tidak-ada-padanannya-di-estimate"></a>
### Fitur Khusus Invoice (TIDAK ADA padanannya di Estimate)

- **Due Date** — tanggal jatuh tempo, bagian dari #17.
- **Payment Terms** — Due on Receipt/Next Day/2 Days/1 Week/Custom, bagian dari #17.
- **Pelacakan Pembayaran** — daftar pembayaran (#25) + tambah pembayaran parsial (#26), lengkap dengan sisa saldo otomatis.
- **Intercept tombol Back** — Daftar Invoice (#7) menampilkan dialog konfirmasi "Exit App?"; Daftar Estimate (#27) tidak (langsung keluar).

*Alasan:* Invoice adalah tagihan resmi (butuh due-date/terms/pelacakan-bayar), Estimate cuma penawaran harga (belum ada uang yang perlu ditagih/dilacak) — jadi field-field ini memang di luar konsep bisnis Estimate, bukan fitur yang "lupa dibuat".

<a id="h-fitur-khusus-estimate-tidak-ada-padanannya-di-invoice"></a>
### Fitur Khusus Estimate (TIDAK ADA padanannya di Invoice)

Tidak ditemukan — secara fungsional, Estimate adalah **subset murni** dari Invoice (semua fiturnya juga ada di Invoice, ditambah Invoice punya due-date/terms/payment yang Estimate tidak punya). Status Open/Closed di Estimate secara struktural setara dengan Paid/Unpaid di Invoice (sama-sama 1 toggle status), bukan fitur tambahan.

---

<a id="h-alur-1"></a>
## Alur 1: Onboarding & Keamanan Awal

Dilalui user **sekali** di awal pemakaian app (atau saat lock aktif setiap kali app dibuka ulang setelah force-stop).

**Cerita alur:** Saat aplikasi dibuka untuk pertama kali, pengguna mendarat di **Splash (#1)**. Sistem mengecek dua kondisi: apakah ini pemakaian pertama, dan apakah kunci PIN sedang aktif. Kalau ini pemakaian pertama, pengguna disambut 3 slide onboarding berisi pengenalan fitur app — bisa digeser satu-satu atau langsung di-skip — sampai menekan tombol "Get Started" yang membawa ke **Setup Profil Bisnis Awal (#2)**. Di sana, dalam 3 langkah berurutan, pengguna mengisi nama & alamat bisnis, menambahkan logo, lalu mengonfirmasi — begitu disimpan, app menandai onboarding selesai dan langsung membawa pengguna ke **Dashboard (#6)**, siap membuat invoice pertama.

Kalau bukan pemakaian pertama tapi kunci PIN aktif, Splash langsung melompat ke **Masukkan PIN (#3)** alih-alih onboarding. PIN yang benar membawa langsung ke Dashboard. Kalau pengguna lupa PIN-nya, tautan "Lupa Password" membuka **Setup Ulang PIN — jawaban keamanan (#4)**, lalu **Simpan PIN Baru (#5)** untuk menetapkan PIN pengganti, sebelum akhirnya kembali mendarat di Dashboard. Pasangan halaman #4→#5 ini bukan cuma dipakai di sini — alur yang sama juga ditempuh ulang kalau pengguna mengaktifkan toggle Lock dari Pengaturan (lihat Alur 8), hanya beda titik kembali di ujungnya.

<a id="h-1"></a>
### 1. Splash / Onboarding (`SplashMainFragment`)
**Fitur:**
- Cek status `isFirstTime` & `lockMode` dari DataStore saat halaman dibuka — kalau first-time, tampilkan onboarding 3-slide (ViewPager2); kalau bukan first-time tapi lock aktif, langsung ke Enter PIN; kalau tidak keduanya, langsung ke Dashboard (tanpa splash tampil).
- Onboarding: 3 slide ilustrasi dengan judul+deskripsi, indikator dot aktif/tidak-aktif, tombol Skip (langsung lanjut), Next (geser slide), dan Get Started (muncul di slide terakhir).
**Widget/Komponen:** `ViewPager2` (adapter `OnboardingAdapter`), indikator dot dinamis, `TextView` Skip, ikon panah Next, `MaterialButton` Get Started. Background selalu gelap (`color_onboarding_background`, fixed, tidak ikut tema app).
**User Story:** Sebagai pemilik bisnis baru pertama kali buka app, saya ingin melihat pengenalan singkat fitur invoice supaya paham apa yang bisa dilakukan app ini sebelum mulai memakainya.
**Navigasi:** Entry: satu-satunya pintu masuk app (`MainActivity.onCreate()` navigasi paksa ke sini saat `savedInstanceState == null`).
→ **lanjut ke:** Halaman 2 (first-time) ATAU Halaman 3 (lock aktif) ATAU langsung Dashboard (#6, popUpTo splash inclusive — splash hilang dari back-stack).

<a id="h-2"></a>
### 2. Setup Profil Bisnis Awal (`AddBusinessDetailsSplashFragment`)
← **dari:** Halaman 1 (jalur first-time)
**Fitur:** Form 3-step (indikator step 1/2/3 di atas) untuk isi profil bisnis pertama kali: nama, alamat, kontak, email, telepon, logo (pilih dari galeri/kamera via bottom sheet). Preview nama & alamat live-update saat mengetik. Tombol back per-step (kembali 1 step) atau keluar fragment kalau di step 1.
**Widget/Komponen:** 3 `View` step (`firstBusinessView`/`secondBusinessView`/`thirdBusinessView`), indikator step berubah warna sesuai step aktif, `ImageView` logo + `BottomSheetDialog` pilih Camera/Gallery, banyak `EditText`, `btnCreateInvoice` (submit akhir).
**User Story:** Sebagai pemilik bisnis baru, saya ingin mengisi profil bisnis saya (nama, logo, alamat) di awal supaya semua invoice/estimate yang saya buat nanti otomatis menampilkan identitas bisnis saya.
**Navigasi:** Back di step 1 → `navigateUp()` (kembali ke Halaman 1).
→ **lanjut ke:** submit (`btnCreateInvoice`) → simpan bisnis, set `isFirstTime=false` → Dashboard (#6, popUpTo splash inclusive).

<a id="h-3"></a>
### 3. Masukkan PIN (`EnterPasswordFragment`)
← **dari:** Halaman 1 (kondisi lock aktif, bukan first-time)
**Fitur:** Input PIN 4-digit via `PinLockView`, validasi terhadap PIN tersimpan. PIN benar → masuk Dashboard. PIN salah → tampil pesan error & reset input. Tautan "Lupa Password" untuk alur recovery.
**Widget/Komponen:** `PinLockView` + `IndicatorDots` (library `com.andrognito.pinlockview`), `TextView` pesan error, `TextView` link lupa password.
**User Story:** Sebagai pengguna yang mengaktifkan kunci aplikasi, saya ingin memasukkan PIN saya supaya data bisnis saya tetap aman dari akses tidak sah.
**Navigasi:** → PIN benar: Dashboard (#6, popUpTo splash inclusive). → "Lupa Password": Halaman 4 (mode RECOVERY).

<a id="h-4"></a>
### 4. Setup Ulang PIN / Pertanyaan Keamanan (`LockMainFragment`)
> ⚠️ **Halaman ini SHARED** — juga dipakai dari Alur 8 (Pengaturan), saat user toggle Lock ON dari Settings (#52). Detail di bawah berlaku untuk kedua entry point; lihat catatan di Alur 8 untuk versi ringkasnya.

← **dari:** Halaman 3 ("Lupa Password", mode RECOVERY) ATAU Halaman 52/`SettingFragment` (toggle Lock ON, mode NEW)
**Fitur:** Input jawaban keamanan (nickname rahasia) untuk mode NEW (aktivasi awal lock) atau RECOVERY (lupa PIN, harus cocok dengan jawaban tersimpan). Validasi non-kosong dan (untuk recovery) kecocokan jawaban.
**Widget/Komponen:** `EditText` jawaban keamanan (`editRecoverAnswer`), `MaterialButton` simpan (`btnSaveSecurity`). Back-button system di-override untuk `navigateUp()`.
**User Story:** Sebagai pengguna yang mengaktifkan PIN, saya ingin mengatur jawaban keamanan supaya saya bisa memulihkan akses kalau lupa PIN nanti.
→ **lanjut ke:** Halaman 5, dengan `pin_type` yang sama (NEW/RECOVERY).

<a id="h-5"></a>
### 5. Simpan PIN Baru (`SavePasswordFragment`)
> ⚠️ **Halaman ini SHARED** — sama seperti Halaman 4, juga dipakai dari Alur 8.

← **dari:** Halaman 4
**Fitur:** Input PIN baru 4-digit + konfirmasi, validasi panjang (harus 4 digit) dan kecocokan kedua input. Simpan PIN & aktifkan lock mode.
**Widget/Komponen:** 2 input PIN (`editNewPassword`, `editConfirmPassword`), `MaterialButton` simpan. Snackbar error untuk validasi gagal.
**User Story:** Sebagai pengguna, saya ingin menetapkan PIN 4-digit baru supaya saya bisa mengunci akses ke aplikasi.
**Navigasi:** → mode NEW dari onboarding: langsung ke Dashboard (#6, popUpTo splash inclusive). → mode NEW dari Settings: kembali ke Halaman 52 (popUpTo+inclusive, bersih dari back stack) — lihat Alur 8. → mode RECOVERY: langsung ke Dashboard (#6, popUpTo splash inclusive).

---

<a id="h-alur-2"></a>
## Alur 2: Dashboard / Ringkasan Utama

**Cerita alur:** Setelah onboarding/PIN selesai (Alur 1), atau kapan saja lewat tab "Dashboard" di navigasi, pengguna mendarat di **Dashboard (#6)** — pusat kendali harian. Empat kartu ringkasan langsung terlihat (total invoice, pendapatan terbayar, tagihan belum dibayar, estimasi terbuka), diikuti daftar invoice terbaru yang bisa ditekan langsung, dan grid Quick Actions yang membuka pintasan ke pembuatan invoice/estimasi baru atau tambah klien/item baru — semua tanpa harus masuk dulu ke tab modul masing-masing. Tombol FAB di pojok kanan bawah menawarkan pilihan cepat serupa lewat bottom sheet. Halaman ini adalah persimpangan: dari sini pengguna bisa melompat ke Alur 3 (Invoice), Alur 4 (Estimate), Alur 5 (Klien), atau Alur 6 (Item) hanya dengan satu tap, dan menekan Back di sini langsung menutup app (bukan berpindah ke halaman lain).

<a id="h-6"></a>
### 6. Dashboard (`DashboardFragment`)
← **dari:** Alur 1 (setelah onboarding/PIN selesai) atau tab "Dashboard" di bottom nav kapan saja
**Fitur:**
- Ringkasan (Overview): 4 card statistik — Total Invoices, Revenue Paid, Outstanding, Open Estimates — tiap card ada angka + trend text (dinamis dari `DashboardViewModel.summary`, meski persentase trend tampak sebagai string siap-pakai dari ViewModel, bukan dihitung ulang di UI).
- Recent Invoices: list max beberapa invoice terbaru (RecyclerView), tap salah satu → navigasi ke modul Invoice; tombol "View All" → modul Invoice. Empty state kalau belum ada invoice.
- Quick Actions grid: New Invoice, New Estimate, Add Client, Add Item — tap langsung ke modul terkait.
- FAB (kanan-bawah) → buka `QuickActionBottomSheet` (opsi New Invoice / New Estimate).
**Widget/Komponen:** `NestedScrollView` pembungkus, `GridLayout` 2 kolom untuk 4 stat card (`MaterialCardView`), `RecyclerView` (`item_recent_invoice`), `GridLayout` 4 kolom Quick Actions, `FloatingActionButton` (`fabQuickAction`), `BottomSheetDialogFragment` custom (`QuickActionBottomSheet`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat ringkasan kondisi bisnis saya (total invoice, pendapatan, tagihan belum dibayar) begitu buka app, supaya saya cepat tahu apa yang perlu ditindaklanjuti.
**Navigasi:** → Alur 3 (Invoice: Recent Invoices/View All/Quick Action New Invoice), → Alur 4 (Estimate: Quick Action), → Alur 5 (Client: Quick Action Add Client), → Alur 6 (Item: Quick Action Add Item). Semua navigasi lintas-modul langsung (`navigate(R.id.xxx_navigation_graph)`), bukan `action` nav-graph biasa. Back dari sini → keluar app (top-level destination).

---

<a id="h-alur-3"></a>
## Alur 3: Invoice End-to-End

Alur ini adalah alur INTI aplikasi: dari melihat daftar invoice sampai selesai membuat & membagikan satu invoice sebagai PDF ke klien.

**Cerita alur:** Menekan tab "Invoice" (atau datang dari Dashboard) membawa pengguna ke **Daftar Invoice (#7)** — tiga tab (Semua #8, Belum Dibayar #9, Lunas #10) yang bisa disaring lewat search bar, plus 3 kartu ringkasan di atasnya. Menekan FAB langsung membuat draft invoice kosong di database dan membawa pengguna ke **Wizard Invoice (#11)**, sebuah shell dengan 3 tab: Edit, Preview, History.

Tab **Edit (#12)** adalah hub utama — dari sinilah hampir seluruh pengisian invoice bercabang: nomor & tanggal invoice (**#17**), info bisnis yang tampil di kop invoice (**#18**), memilih atau memasukkan klien (**#19 → #20**), menambahkan item satu-satu atau mencari dari katalog tersimpan (**#21 → #22**), membubuhkan tanda tangan digital (**#15**), melampirkan foto (**#16**), menentukan diskon (**#23**) dan pajak (**#24**) untuk keseluruhan invoice, serta mencatat pembayaran yang sudah diterima (**#25 → #26**). Semua cabang ini, setelah selesai diisi, kembali lagi ke hub #12 — polanya seperti jari-jari roda, bukan rantai linear.

Setelah semua data lengkap, pengguna berpindah ke tab **Preview (#13)** untuk melihat invoice sebagai dokumen utuh persis seperti yang akan diterima klien, lalu menekan tombol Share di toolbar untuk membagikannya sebagai PDF lewat Android Share Sheet. Tab **History (#14)**, yang seharusnya mencatat riwayat pembagian ini, saat ini masih kosong (belum diimplementasikan). Menekan Back dari Daftar Invoice (#7) memunculkan dialog konfirmasi "Exit App?" — bukan langsung keluar seperti kebanyakan layar lain.

<a id="h-7"></a>
### 7. Daftar Invoice (`InvoiceMainFragment`)
← **dari:** tab "Invoice" bottom nav, atau dari Dashboard (#6, Recent Invoices/Quick Action)
**Fitur:** 3 tab (All/Outstanding/Paid) dengan hitungan jumlah di label tab, live-update dari `allInvoicesLive`. 3 summary card di atas tab (Total Invoices, Outstanding, Paid) dengan warna kontekstual. Search bar (`editSearch`) yang memfilter via `viewModel.updateSearchQuery()`. FAB tambah invoice baru (`btnAddInvoice`) — langsung insert record kosong ke DB lalu navigasi ke wizard Edit. **Back-button di-intercept → dialog custom "Exit App?"** (bukan behavior standar Android — lihat Catatan & Temuan).
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapter`, host 3 tab fragment di bawah), `view_invoice_summary_card` x3, `EditText` search, `FloatingActionButton`, `Dialog` custom exit (`exit_dialog.xml`).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua invoice saya dikelompokkan per status (Semua/Belum Dibayar/Lunas) supaya saya bisa memantau mana yang perlu ditagih.
→ **lanjut ke:** FAB → Halaman 11 (wizard, mode BARU). Tap item invoice (di tab manapun) → Halaman 11 (wizard, mode EDIT).

<a id="h-8"></a>
#### 8. Tab "All" (`AllInvoices`)
**Fitur:** List semua invoice (RecyclerView), diurutkan terbaru dulu. Empty state kalau kosong.
**Widget/Komponen:** `RecyclerView` (adapter `AllInvoiceAdapter`), Empty State merge-include.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat seluruh invoice tanpa filter supaya saya punya gambaran lengkap.
**Navigasi:** Tab pertama Halaman 7. Tap item → Halaman 11.

<a id="h-9"></a>
#### 9. Tab "Outstanding" (`OutstandingInvoices`)
**Fitur:** List invoice difilter status `UN_PAID` saja. Empty state manual (TextView, bukan merge-include).
**User Story:** Sebagai pemilik bisnis, saya ingin fokus melihat invoice yang belum dibayar supaya saya tahu siapa yang perlu ditagih.
**Navigasi:** Sama seperti Tab All.

<a id="h-10"></a>
#### 10. Tab "Paid" (`PaidInvoices`)
**Fitur:** List invoice berstatus `PAID` saja.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat invoice yang sudah lunas sebagai catatan pendapatan yang sudah masuk.
**Navigasi:** Sama seperti Tab All.

<a id="h-11"></a>
### 11. Wizard Invoice — shell (`AddInvoiceMainFragment`)
← **dari:** Halaman 7 (FAB atau tap item)
**Fitur:** Wrapper dengan 3 tab internal (Edit/Preview/History via ViewPager2). Toolbar: back (simpan otomatis lalu keluar), menu titik-tiga (Delete/Share), tombol Share Invoice (generate PDF via WebView→PDF, share via Android Share Sheet). Delete invoice via `BaseDialog` konfirmasi.
**Widget/Komponen:** `TabLayout`+`ViewPager2` (adapter `ViewPagerAdapterEditInvoice`), toolbar lama, `PopupMenu` (Delete/Share), `WebView` tersembunyi (render HTML→PDF), `Snackbar` (izin storage ditolak).
**User Story:** Sebagai pemilik bisnis, saya ingin mengedit detail invoice, melihat pratinjau hasil akhirnya, dan membagikannya sebagai PDF ke klien saya.
**Navigasi:** Keluar (back/toolbar-back) → simpan seluruh field ke DB → `navigateUp()` (balik ke Halaman 7). Delete → konfirmasi → `navigateUp()`.
→ **lanjut ke:** Halaman 12 (tab pertama, hub utama wizard).

<a id="h-12"></a>
#### 12. Tab "Edit" (`EditInvoiceFragment`) — HUB WIZARD
← **dari:** Halaman 11 (tab pertama)
**Fitur:** Form utama invoice — nomor & tanggal invoice, info bisnis, pilih klien, daftar item (tambah/lihat), signature, foto lampiran, diskon, pajak, catatan, instruksi pembayaran, toggle status Mark Paid/Unpaid, link ke daftar pembayaran.
**Widget/Komponen:** Banyak `TextView` clickable sebagai "field" (kecuali Notes & Payment Instruction yang sudah `TextInputLayout`), `RecyclerView` item (`SelectedInvoiceItemsAdapter`) & foto (`SelectedPhotosForInvoiceAdapter`), `MaterialButton` Mark Paid/Unpaid.
**User Story:** Sebagai pemilik bisnis, saya ingin mengisi semua detail invoice (klien, item, diskon, pajak) dalam satu layar terpusat supaya proses pembuatan invoice cepat.
**Navigasi:** Ini HUB — semua sub-halaman berikut (13 kecuali, 15–26) dibuka dari sini via tap field, dan SEMUA kembali ke sini setelah selesai (`navigateUp()`). Urutan tap field yang lazim: Info Invoice (17) → Business Detail (18) → Klien (19/20) → Item (21/22) → Signature (15) → Foto (16) → Diskon (23) → Pajak (24) → Pembayaran (25/26).

<a id="h-13"></a>
#### 13. Tab "Preview" (`PreviewInvoiceFragment`)
**Fitur:** Render pratinjau invoice sebagai halaman HTML lengkap (logo, klien, tabel item, subtotal/pajak/diskon/total, instruksi pembayaran, foto, tanda tangan, kontak bisnis) di `WebView` — inilah sumber PDF saat "Share".
**User Story:** Sebagai pemilik bisnis, saya ingin melihat persis seperti apa invoice akan terlihat sebelum dikirim ke klien.
**Navigasi:** Tab kedua Halaman 11, tidak ada navigasi keluar selain ganti tab.

<a id="h-14"></a>
#### 14. Tab "History" (`InvoiceShareHistoryFragment`)
**Fitur:** ⚠️ **Kosong — belum diimplementasikan.** Layout ter-inflate tapi tidak ada logic/data.
**User Story:** *(belum terpenuhi)* Sebagai pemilik bisnis, saya ingin melihat riwayat kapan/ke mana invoice ini pernah dibagikan.
**Navigasi:** Tab ketiga Halaman 11.

<a id="h-15"></a>
#### 15. Tanda Tangan (`SignatureFragment`)
← **dari:** Halaman 12 (txtSignature)
**Fitur:** Kanvas gambar tanda tangan (`SignatureView`), tombol Clear, OK (simpan ke ViewModel sementara), Cancel.
**User Story:** Sebagai pemilik bisnis, saya ingin menandatangani invoice secara digital supaya terlihat lebih resmi.
→ **kembali ke:** Halaman 12 (OK/Cancel/toolbar-back).

<a id="h-16"></a>
#### 16. Tambah Foto (`AddPhotoToInvoiceFragment`)
← **dari:** Halaman 12 (txtAddPhoto, argumen `photo_type` NEW/OLD)
**Fitur:** Pilih/ambil foto (kamera/galeri via bottom sheet), isi deskripsi & detail tambahan, simpan atau hapus (foto lama, via `BaseDialog`). Validasi wajib pilih foto dulu.
**Widget/Komponen:** `ImageView` foto (klik untuk pilih ulang), `BottomSheetDialog` Camera/Gallery, `EditText` deskripsi & detail.
**User Story:** Sebagai pemilik bisnis, saya ingin melampirkan foto (misal bukti kerja/barang) ke invoice supaya klien punya konteks visual.
→ **kembali ke:** Halaman 12 (simpan otomatis atau setelah hapus).

<a id="h-17"></a>
#### 17. Info Invoice (`InvoiceInformationFragment`)
← **dari:** Halaman 12 (txtInvoiceDate/txtInvoiceNumber/txtDueOnReceipt)
**Fitur:** Edit nomor invoice, tanggal invoice (date picker), tanggal jatuh tempo (date picker), PO number, Terms (bottom sheet: None/Due on Receipt/Next Day/2 Days/1 Week/Custom). Validasi nomor invoice wajib diisi.
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur nomor invoice, tanggal, dan syarat pembayaran supaya invoice sesuai kebutuhan administrasi saya.
→ **kembali ke:** Halaman 12 (simpan).

<a id="h-18"></a>
#### 18. Detail Bisnis dari Invoice (`EditBusinessDetailsFromInvoiceFragment`)
← **dari:** Halaman 12 (txtBusinessInfo)
**Fitur:** Edit profil bisnis (nama, pemilik, nomor bisnis, alamat, email, telepon, website, logo) langsung dari konteks invoice — perubahan memengaruhi profil bisnis global.
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui info bisnis saya (misal alamat baru) langsung dari layar invoice tanpa harus ke Settings.
> Catatan: ini Fragment TERPISAH dari halaman Profil Bisnis di Alur 8 (#53) dan dari setup awal di Alur 1 (#2) — bukan reuse class yang sama, tapi fungsinya identik (edit tabel Business yang sama). Lihat Alur 8 untuk versi "berdiri sendiri"-nya.
→ **kembali ke:** Halaman 12 (simpan).

<a id="h-19"></a>
#### 19. Daftar Klien untuk Invoice (`ClientListForInvoiceFragment`)
← **dari:** Halaman 12 (secondCard, kalau belum ada klien terpilih & daftar klien tidak kosong)
**Fitur:** List semua klien (dari cache ViewModel) untuk dipilih sebagai klien invoice ini.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih klien yang sudah ada dari daftar supaya tidak perlu mengetik ulang datanya.
> Catatan: Fragment terpisah dari Alur 5 (Manajemen Klien) — daftar di sini untuk *memilih*, bukan CRUD penuh. Data klien yang dikelola tetap sama (lihat Alur 5 untuk tambah/edit/hapus klien permanen).
→ **kembali ke:** Halaman 12 (tap klien → set `selectedClient` → `navigateUp()`).

<a id="h-20"></a>
#### 20. Detail Klien untuk Invoice (`ClientDetailForInvoiceFragment`)
← **dari:** Halaman 12 (secondCard, kalau sudah ada klien terpilih ATAU daftar klien kosong)
**Fitur:** Form detail klien — dipakai untuk melihat/edit klien yang dipilih, ATAU input klien baru ad-hoc (tanpa disimpan ke tabel Client permanen — `clientId` di-set `Int.MAX_VALUE` sebagai penanda sementara). Tombol Delete (toolbar) untuk membatalkan pilihan klien.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat/menyesuaikan detail klien yang dipilih untuk invoice ini, atau memasukkan klien baru langsung di tempat.
→ **kembali ke:** Halaman 12 (simpan ke `selectedClient` atau clear via Delete).

<a id="h-21"></a>
#### 21. Detail Item untuk Invoice (`ItemDetailForInvoiceFragment`)
← **dari:** Halaman 12 (txtAddItem, item baru) atau dari list item existing (edit)
**Fitur:** Form item invoice — deskripsi, harga satuan, kuantitas (kalkulasi total real-time via `ItemCalculator`), jenis diskon (bottom sheet: Percentage/Flat Amount), jumlah diskon, toggle kena-pajak (+tax rate), detail tambahan. Icon search (khusus item NEW) → Halaman 22. Delete (item lama) via `BaseDialog`.
**Widget/Komponen:** `TextInputLayout` untuk Description & Additional Details, `Switch` kena-pajak.
**User Story:** Sebagai pemilik bisnis, saya ingin menambahkan item ke invoice dengan harga, jumlah, dan diskon supaya totalnya terhitung otomatis.
→ **lanjut ke:** Halaman 22 (search icon) atau **kembali ke:** Halaman 12 (simpan/hapus).

<a id="h-22"></a>
#### 22. Daftar Item Tersimpan untuk Invoice (`ItemsListForInvoice`)
← **dari:** Halaman 21 (icon search)
**Fitur:** List semua item dari master data Item (Alur 6), untuk dipilih cepat mengisi form Item Detail.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih item dari daftar item master saya supaya tidak perlu mengetik ulang nama & harga tiap kali buat invoice.
→ **kembali ke:** Halaman 21 (tap item → isi form → `navigateUp()`).

<a id="h-23"></a>
#### 23. Diskon Invoice (`DiscountFragment`)
← **dari:** Halaman 12 (txtDiscountPrice)
**Fitur:** Pilih jenis diskon keseluruhan invoice (bottom sheet: No Discount/Percentage/Flat Amount) + jumlah diskon, hitung via `DiscountCalculator` berdasar subtotal invoice.
**User Story:** Sebagai pemilik bisnis, saya ingin memberi diskon keseluruhan pada invoice (persentase atau nominal tetap) untuk klien tertentu.
→ **kembali ke:** Halaman 12 (simpan).

<a id="h-24"></a>
#### 24. Pajak Invoice (`TaxFragment`)
← **dari:** Halaman 12 (txtTaxPrice)
**Fitur:** Pilih jenis pajak (bottom sheet: None/On The Total/Deducted/Per Item), label pajak custom, tarif pajak, toggle "tax inclusive". Field berubah show/hide sesuai jenis pajak.
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur pajak yang berlaku pada invoice sesuai kebijakan pajak lokal saya.
→ **kembali ke:** Halaman 12 (simpan).

<a id="h-25"></a>
#### 25. Daftar Pembayaran (`InvoicePaymentsListFragment`)
← **dari:** Halaman 12 (txtPayment/txtPaymentPrice)
**Fitur:** List pembayaran tercatat untuk invoice ini, total dibayar & sisa saldo dihitung otomatis.
**User Story:** Sebagai pemilik bisnis, saya ingin mencatat pembayaran cicilan/parsial yang diterima untuk invoice ini dan melihat sisa tagihannya.
→ **lanjut ke:** Halaman 26 ("Add Payment") atau **kembali ke:** Halaman 12 (toolbar-back).

<a id="h-26"></a>
#### 26. Tambah Pembayaran (`InvoiceAddPaymentFragment`)
← **dari:** Halaman 25 ("Add Payment")
**Fitur:** Input jumlah pembayaran, tanggal (default hari ini, date picker), metode pembayaran (bottom sheet: Cash/Cheque/Bank/Credit Card/PayPal/Other), catatan. Validasi jumlah wajib diisi.
**User Story:** Sebagai pemilik bisnis, saya ingin mencatat detail satu transaksi pembayaran (jumlah, tanggal, metode) yang diterima dari klien.
→ **kembali ke:** Halaman 25 (simpan ke list pembayaran).

---

<a id="h-alur-4"></a>
## Alur 4: Estimate End-to-End

> Estimate secara struktural adalah duplikasi 1:1 dari Alur 3 (Invoice), dengan perbedaan: **tidak ada** konsep due-date/terms/payment-tracking (estimasi bukan tagihan), status hanya Open/Closed (bukan Paid/Unpaid), dan module ini **belum** mendapat redesign visual (summary card/search bar) seperti Halaman 7 — masih toolbar lama sepenuhnya. Karena strukturnya identik, halaman yang isinya sama persis dengan padanan Invoice-nya ditulis ringkas dengan rujukan silang.

**Cerita alur:** Perjalanannya menempuh langkah yang nyaris identik dengan Invoice, hanya untuk penawaran harga sebelum menjadi tagihan resmi. Dari **Daftar Estimate (#27)** (tab All/Open/Closed: **#28–#30**), FAB membawa ke **Wizard Estimate (#31)**, dengan hub Edit di **#32** yang mengumpulkan info estimasi (**#36**), profil bisnis (**#37**), klien (**#38 → #39**), item (**#40 → #41**), tanda tangan (**#35**), dan foto (**#44**), diskon (**#42**), dan pajak (**#43**) — tanpa fitur due-date/terms/payment yang ada di Invoice, karena estimasi memang bukan tagihan resmi. Setelah lengkap, tab **Preview (#33)** menampilkan hasil akhir sebelum dibagikan sebagai PDF lewat toolbar Share. Tab **History (#34)** juga masih kosong, sama seperti pada Invoice. Bedanya dari Invoice: Estimate belum mendapat pembaruan visual (search bar, kartu ringkasan), dan menekan Back dari Daftar Estimate (#27) langsung keluar tanpa dialog konfirmasi — beda perilaku dari Daftar Invoice (#7).

<a id="h-27"></a>
### 27. Daftar Estimate (`EstimatesMainFragment`)
← **dari:** tab "Estimate" bottom nav, atau Dashboard (#6) Quick Action
**Fitur:** 3 tab (All/Open/Closed) dengan hitungan jumlah. Ringkasan jumlah (total/open/closed). FAB tambah estimate baru. **Tidak ada** intercept back-button khusus (beda dari Halaman 7).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua estimasi/penawaran harga yang pernah saya buat, dikelompokkan status terbuka/tertutup.
→ **lanjut ke:** FAB/tap item → Halaman 31 (wizard).

<a id="h-28"></a>
#### 28. Tab "All" (`AllEstimatesFragment`)
**Fitur:** List semua estimate, urut terbaru dulu. Setara Halaman 8.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat seluruh estimasi tanpa filter status.

<a id="h-29"></a>
#### 29. Tab "Open" (`OpenEstimatesFragment`)
**Fitur:** List estimate status `OPEN` saja. Setara Halaman 9.
**User Story:** Sebagai pemilik bisnis, saya ingin fokus ke estimasi yang masih menunggu keputusan klien.

<a id="h-30"></a>
#### 30. Tab "Closed" (`ClosedEstimatesFragment`)
**Fitur:** List estimate status `CLOSED` saja. Setara Halaman 10.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat riwayat estimasi yang sudah selesai diproses (diterima/ditolak/kadaluarsa).

<a id="h-31"></a>
### 31. Wizard Estimate — shell (`AddEstimateMainFragment`)
← **dari:** Halaman 27
**Fitur:** Setara Halaman 11 — 3 tab (Edit/Preview/History), toolbar back (auto-save), menu Delete/Share PDF.
**User Story:** Sebagai pemilik bisnis, saya ingin menyusun estimasi harga untuk calon klien dan membagikannya sebagai PDF.
→ **lanjut ke:** Halaman 32 (hub).

<a id="h-32"></a>
#### 32. Tab "Edit" (`EditEstimateFragment`) — HUB WIZARD ESTIMATE
← **dari:** Halaman 31
**Fitur:** Setara Halaman 12 tapi TANPA Payment/Terms/Due-Date — nomor & tanggal estimasi, info bisnis, klien, item, signature, foto, diskon, pajak, catatan, toggle status Mark Open/Closed.
**User Story:** Sebagai pemilik bisnis, saya ingin mengisi detail estimasi (klien, item, harga) dalam satu layar.
**Navigasi:** HUB — sub-halaman 35–44 dibuka dari sini, semua kembali ke sini setelah selesai.

<a id="h-33"></a>
#### 33. Tab "Preview" (`PreviewEstimateFragment`)
**Fitur:** Setara Halaman 13, tanpa bagian pembayaran.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat tampilan akhir estimasi sebelum dikirim ke calon klien.

<a id="h-34"></a>
#### 34. Tab "History" (`EstimateShareHistoryFragment`)
**Fitur:** ⚠️ Kosong — setara Halaman 14, belum diimplementasikan.
**User Story:** *(belum terpenuhi)* Melihat riwayat pengiriman estimasi.

<a id="h-35"></a>
#### 35. Tanda Tangan Estimate (`EstimateSignatureFragment`)
← **dari:** Halaman 32 (txtSignature) — Identik Halaman 15.
**User Story:** Sebagai pemilik bisnis, saya ingin menandatangani estimasi secara digital.
→ **kembali ke:** Halaman 32.

<a id="h-36"></a>
#### 36. Info Estimate (`EstimateInformationFragment`)
← **dari:** Halaman 32 (txtEstimateDate/txtEstimateNumber)
**Fitur:** Edit nomor estimasi, tanggal (date picker), PO number. **Tidak ada** field due-date/terms (beda dari Halaman 17).
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur nomor dan tanggal estimasi.
→ **kembali ke:** Halaman 32.

<a id="h-37"></a>
#### 37. Detail Bisnis dari Estimate (`EditBusinessDetailsFromEstimateFragment`)
← **dari:** Halaman 32 (txtBusinessInfo) — Identik Halaman 18 (Fragment terpisah, fungsi sama).
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui profil bisnis dari konteks estimasi.
→ **kembali ke:** Halaman 32.

<a id="h-38"></a>
#### 38. Daftar Klien untuk Estimate (`ClientListForEstimateFragment`)
← **dari:** Halaman 32 (secondCard) — Identik Halaman 19, adapter `AllClientsForEstimateAdapter`.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih klien dari daftar untuk estimasi ini.
→ **kembali ke:** Halaman 32.

<a id="h-39"></a>
#### 39. Detail Klien untuk Estimate (`ClientDetailForEstimateFragment`)
← **dari:** Halaman 32 (secondCard) — Identik Halaman 20.
**User Story:** Sebagai pemilik bisnis, saya ingin menyesuaikan/menghapus data klien pada estimasi ini.
→ **kembali ke:** Halaman 32.

<a id="h-40"></a>
#### 40. Detail Item untuk Estimate (`ItemDetailForEstimateFragment`)
← **dari:** Halaman 32 (txtAddItem) atau list item existing — Identik Halaman 21 (kalkulasi via `ItemCalculator`).
**User Story:** Sebagai pemilik bisnis, saya ingin menambahkan item dengan harga & diskon ke estimasi.
→ **lanjut ke:** Halaman 41 (tombol search — *pola serupa Invoice, belum 100% diverifikasi ulang*) atau **kembali ke:** Halaman 32.

<a id="h-41"></a>
#### 41. Daftar Item Tersimpan untuk Estimate (`ItemListForEstimate`)
← **dari:** Halaman 40 — Identik Halaman 22, adapter `AllItemsForEstimateAdapter`.
**User Story:** Sebagai pemilik bisnis, saya ingin memilih item dari master data untuk estimasi ini.
→ **kembali ke:** Halaman 40.

<a id="h-42"></a>
#### 42. Diskon Estimate (`EstimateDiscountFragment`)
← **dari:** Halaman 32 (txtDiscountPrice) — Identik Halaman 23, berbasis `estimateSubTotal`.
**User Story:** Sebagai pemilik bisnis, saya ingin memberi diskon keseluruhan pada estimasi.
→ **kembali ke:** Halaman 32.

<a id="h-43"></a>
#### 43. Pajak Estimate (`EstimateTaxFragment`)
← **dari:** Halaman 32 (txtTaxPrice) — Identik Halaman 24.
**User Story:** Sebagai pemilik bisnis, saya ingin mengatur pajak yang berlaku pada estimasi.
→ **kembali ke:** Halaman 32.

<a id="h-44"></a>
#### 44. Tambah Foto Estimate (`AddPhotoToEstimateFragment`)
← **dari:** Halaman 32 (txtAddPhoto) — Identik Halaman 16.
**User Story:** Sebagai pemilik bisnis, saya ingin melampirkan foto ke estimasi.
→ **kembali ke:** Halaman 32.

---

<a id="h-alur-5"></a>
## Alur 5: Manajemen Klien

Data klien di sini dipakai ulang oleh Alur 3 (Halaman 19–20) dan Alur 4 (Halaman 38–39) saat memilih klien untuk invoice/estimasi — tapi halaman *pemilihan* di wizard adalah Fragment terpisah dari CRUD penuh di bawah ini.

**Cerita alur:** Diakses dari menu More atau Quick Action Dashboard, **Daftar Klien (#45)** menampilkan seluruh klien yang pernah disimpan. Menekan tombol tambah, atau salah satu klien di daftar, membawa ke **Tambah/Edit Klien (#46)** — form sederhana yang otomatis tersimpan begitu pengguna menekan tombol kembali (asal nama sudah diisi). Data yang dikelola di sini kemudian muncul sebagai pilihan cepat saat pengguna memilih klien di dalam wizard Invoice (#19–#20) maupun Estimate (#38–#39) — meski halaman pemilihan di wizard tersebut Fragment yang terpisah, bukan alur ini yang dipanggil ulang.

<a id="h-45"></a>
### 45. Daftar Klien (`ClientMainFragment`)
← **dari:** Alur 9/More (#54, menuClients) atau Dashboard (#6, Quick Action Add Client — langsung ke sini, bukan ke form tambah)
**Fitur:** List semua klien (RecyclerView), empty state kalau kosong. Tombol tambah klien baru.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat semua klien saya dalam satu daftar supaya mudah memilihnya saat buat invoice/estimasi.
→ **lanjut ke:** FAB → Halaman 46 (mode BARU). Tap item → Halaman 46 (mode EDIT).

<a id="h-46"></a>
### 46. Tambah/Edit Klien (`AddClient`)
← **dari:** Halaman 45
**Fitur:** Form klien (nama, email, kontak, telepon/mobile/fax, 3 baris alamat). Auto-save saat back (kalau nama diisi). Menu titik-tiga (khusus edit) → Delete via `BaseDialog`.
**User Story:** Sebagai pemilik bisnis, saya ingin menambah klien baru atau memperbarui data klien yang sudah ada.
→ **kembali ke:** Halaman 45 (simpan/hapus).

---

<a id="h-alur-6"></a>
## Alur 6: Manajemen Item/Barang

Data item di sini dipakai ulang oleh Alur 3 (Halaman 21–22) dan Alur 4 (Halaman 40–41).

**Cerita alur:** Serupa dengan alur Klien, **Daftar Item (#47)** menampilkan katalog item/jasa yang dijual, dan **Tambah/Edit Item (#48)** untuk mengelolanya (harga satuan, status kena-pajak). Item-item ini menjadi pilihan cepat saat pengguna mengisi baris item di dalam wizard Invoice (#22) maupun Estimate (#41), lewat fitur cari-dari-katalog.

<a id="h-47"></a>
### 47. Daftar Item (`ItemMainFragment`)
← **dari:** Alur 9/More (#54, menuItems) atau Dashboard (#6, Quick Action Add Item)
**Fitur:** List semua item master (RecyclerView), empty state kalau kosong. Tombol tambah item baru.
**User Story:** Sebagai pemilik bisnis, saya ingin mengelola katalog item/jasa yang biasa saya jual supaya cepat dipilih saat membuat invoice.
→ **lanjut ke:** Halaman 48 (mode BARU/EDIT).

<a id="h-48"></a>
### 48. Tambah/Edit Item (`AddItemFragment`)
← **dari:** Halaman 47
**Fitur:** Form item (deskripsi, harga satuan, toggle kena-pajak, detail tambahan). Auto-save saat back. Menu titik-tiga (khusus edit) → Delete via `BaseDialog`.
**User Story:** Sebagai pemilik bisnis, saya ingin menambah atau memperbarui item di katalog saya (harga, status kena pajak).
→ **kembali ke:** Halaman 47 (simpan/hapus).

---

<a id="h-alur-7"></a>
## Alur 7: Laporan (Reports)

**Cerita alur:** Tab "Reports" membawa ke **Shell Reports (#49)** dengan dua tab murni-baca: **Paid (#50)**, ringkasan invoice yang sudah lunas beserta rata-rata nilainya, dan **Clients (#51)**, ranking klien berdasarkan total tagihan. Tidak ada navigasi lebih jauh dari sini selain berpindah tab — alur ini adalah titik akhir, bukan persimpangan ke alur lain.

<a id="h-49"></a>
### 49. Shell Reports (`ReportsMainFragment`)
← **dari:** tab "Reports" bottom nav
**Fitur:** 2 tab (Paid/Clients) via ViewPager2+TabLayout dengan ikon per tab.
**User Story:** Sebagai pemilik bisnis, saya ingin melihat laporan ringkas soal invoice yang lunas dan performa per klien.
**Navigasi:** Tidak ada navigasi keluar selain ganti tab.

<a id="h-50"></a>
#### 50. Tab "Paid" (`PaidReportFragment`)
**Fitur:** List semua invoice lunas + kartu ringkasan (total invoice lunas, total nilai dibayar, rata-rata per invoice, jumlah klien unik).
**User Story:** Sebagai pemilik bisnis, saya ingin melihat total pendapatan yang sudah masuk dan performa rata-rata invoice saya.

<a id="h-51"></a>
#### 51. Tab "Clients" (`ClientsReportFragment`)
**Fitur:** List ringkasan performa per klien (total tagihan per klien).
**User Story:** Sebagai pemilik bisnis, saya ingin tahu klien mana yang paling banyak berkontribusi ke pendapatan saya.

---

<a id="h-alur-8"></a>
## Alur 8: Pengaturan & Profil Bisnis

**Cerita alur:** Diakses lewat ikon Settings di header atau menu More, **Pengaturan (#52)** berisi toggle Dark Mode (langsung berefek begitu ditekan), toggle Lock, dan tautan ke **Profil Bisnis (#53)**. Mengaktifkan toggle Lock menempuh ULANG alur setup PIN dari Alur 1 (**#4 → #5**) — jawaban keamanan lalu PIN baru — tapi kali ini kembali ke halaman Pengaturan ini setelah selesai, bukan ke Dashboard seperti saat onboarding. Menekan "Personal Business" membawa ke Profil Bisnis (#53), tempat pengguna memperbarui identitas bisnis (nama, alamat, logo) kapan saja di luar konteks onboarding awal — isinya sama seperti form onboarding (#2), tapi ini Fragment yang berbeda.

<a id="h-52"></a>
### 52. Pengaturan (`SettingFragment`)
← **dari:** ikon Settings di header (tersedia di semua layar top-level) atau Alur 9/More (#54, menuSettings)
**Fitur:** Link ke Profil Bisnis. Toggle Lock (aktifkan → alur setup PIN baru; nonaktifkan → langsung matikan lock mode). Toggle Dark Mode (langsung terapkan tema via `AppCompatDelegate`, simpan preferensi).
**User Story:** Sebagai pengguna, saya ingin mengatur preferensi aplikasi (keamanan PIN, tema gelap/terang) dan mengakses profil bisnis saya.
→ **lanjut ke:** "Personal Business" → Halaman 53. Toggle Lock ON → **Halaman 4** (Alur 1, mode NEW — lihat Alur 1 untuk detail lengkap alur setup PIN-nya, kembali ke sini setelah PIN tersimpan via popUpTo+inclusive).

<a id="h-53"></a>
### 53. Profil Bisnis (`AddBusinessDetailsFragment`)
← **dari:** Halaman 52
**Fitur:** Sama seperti setup bisnis awal (Halaman 2) — nama, alamat, kontak, email, telepon, website, logo — tapi dalam konteks edit dari Settings, bukan onboarding.
**User Story:** Sebagai pemilik bisnis, saya ingin memperbarui profil bisnis saya kapan saja lewat menu Pengaturan.
→ **kembali ke:** Halaman 52 (simpan).

---

<a id="h-alur-9"></a>
## Alur 9: Navigasi "More"

**Cerita alur:** Tab "More" membuka **Menu Lainnya (#54)** berisi 11 pintasan yang dikelompokkan (Business, Tools, Support). Hanya 3 yang benar-benar mengarah kemana-mana — Clients (ke Alur 5, #45), Items (ke Alur 6, #47), dan Settings (ke Alur 8, #52) — sisanya 8 menu (Backup & Restore, Export Data, PDF Templates, Help Center, Feedback, Rate App, About, Logout) tampil lengkap secara visual namun menekannya tidak melakukan apa-apa. Alur ini murni berfungsi sebagai "pintu belakang" ke alur-alur lain, bukan tujuan akhir tersendiri.

<a id="h-54"></a>
### 54. Menu Lainnya (`MoreFragment`)
← **dari:** tab "More" bottom nav
**Fitur:** 11 menu item dikelompokkan (Business: Clients, Items; Tools: Backup & Restore, Export Data, PDF Templates, Settings; Support: Help Center, Feedback, Rate App, About; Logout). ⚠️ **Hanya 3 yang berfungsi** (Clients, Items, Settings) — 8 sisanya tampil visual lengkap tapi tidak ada click listener (dead UI).
**User Story:** Sebagai pengguna, saya ingin mengakses fitur-fitur sekunder (kelola klien/item, pengaturan, bantuan) dari satu menu terpusat.
**Navigasi:** → Clients: Alur 5 (#45). → Items: Alur 6 (#47). → Settings: Alur 8 (#52). 8 menu lain: tidak ada navigasi (dead UI).

---

<a id="h-catatan"></a>
## Catatan & Temuan

Dirangkum dari `PAGES.md`, supaya tidak hilang dari revisi pengelompokan-ulang ini:

1. **Shell UI (`activity_main.xml`+`MainActivity.kt`):** header baru (ikon Settings + greeting "Hello, John" + judul + notification bell badge "3") dan `BottomNavigationView` diposisikan **di atas** (overlap header), bukan di bawah layar seperti konvensi Android umum. Notification bell **dekoratif, tidak ada click listener**.
2. **Toolbar lama** (`include layout="toolbar"`) masih ada di XML tapi `visibility="gone"` permanen — banyak Fragment level-dalam (hampir semua halaman wizard Invoice/Estimate, plus Client/Item/Reports/Settings) masih memanggil `(activity as MainActivity).binding?.toolbar` untuk judul/tombol back, padahal toolbar itu sudah invisible. Judul/tombol back di halaman-halaman itu kemungkinan tidak tampil visual kecuali header baru kebetulan menaunginya. Pengamatan struktural, belum diperbaiki.
3. Header baru (greeting+bell) hanya disembunyikan untuk 32 destination yang didaftarkan eksplisit di `MainActivity`. Untuk destination lain (Client/Item/Reports/Settings list), header baru tetap tampil.
4. **`InvoiceMainFragment` (Halaman 7) TAPI TIDAK `EstimatesMainFragment` (Halaman 27)** meng-intercept tombol Back dengan dialog custom "Exit App?" — inkonsistensi behavior back-button antara dua alur yang seharusnya paralel.
5. **Tab "History"** di wizard Invoice (Halaman 14) dan Estimate (Halaman 34) sama-sama placeholder kosong — belum diimplementasikan sepenuhnya.
6. **Alur 9/More (Halaman 54):** 8 dari 11 menu adalah dead UI (Backup & Restore, Export Data, PDF Templates, Help Center, Feedback, Rate App, About, Logout) — tampil lengkap secara visual tapi tap tidak melakukan apa-apa.
7. **Estimate (Alur 4) belum mendapat redesign visual** yang sama seperti Invoice (Alur 3) — masih toolbar lama sepenuhnya, `imgSearch` di layar daftar tampil tapi tidak difungsikan jadi search box nyata.
8. **Halaman 40 → 41** (Item Detail Estimate ke daftar item tersimpan): wiring tombol search belum 100% diverifikasi ulang terhadap kode — diasumsikan sama seperti pola Invoice (Halaman 21→22) berdasar kemiripan struktural yang kuat di seluruh modul Estimate, tapi belum ditelusuri baris-demi-baris.
9. **2 Fragment sengaja tidak didokumentasikan:** `HomeFragment` dan `NotificationsFragment` — terkonfirmasi tidak tersambung ke navigation graph manapun, sisa scaffolding default Android Studio.
10. **Halaman yang genuinely shared (Fragment class sama dipakai dari 2 alur berbeda):** hanya Halaman 4 (`LockMainFragment`) dan Halaman 5 (`SavePasswordFragment`), dipakai baik dari Alur 1 (onboarding) maupun Alur 8 (Settings). Semua kemiripan lain antar-alur (misal Client-picker di wizard vs Client management, atau Business-Detail-edit di 3 tempat berbeda) adalah Fragment class yang BERBEDA meski fungsinya serupa — bukan reuse kode yang sama.
