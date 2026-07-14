# Koinvois Modernization — Sprint Status

Roadmap 9 sprint untuk modernisasi incremental aplikasi Android Koinvois (Invoice Generator, offline-first, Kotlin, Clean Architecture + MVVM). Bukan rewrite — modernisasi bertahap dari codebase existing.

> Repo ini **belum di-`git init`** (keputusan sadar user) — tidak ada rollback safety net antar sprint.

## Status ringkas

| # | Sprint | Status |
|---|--------|--------|
| 1 | Project Foundation | ✅ Selesai |
| 2 | Offline Database | ✅ Selesai — `BUILD SUCCESSFUL` |
| 3 | Business Profile module | ✅ Selesai — `BUILD SUCCESSFUL` |
| 4 | Client module | ✅ Selesai — `BUILD SUCCESSFUL` |
| 5 | Item module | ✅ Selesai — `BUILD SUCCESSFUL` |
| 6 | Invoice module | ✅ Selesai — `BUILD SUCCESSFUL` |
| 7 | Estimate module | ✅ Selesai — `BUILD SUCCESSFUL` |
| 8 | Dashboard | ✅ Selesai — `BUILD SUCCESSFUL` |
| 9 | Reports | ✅ Selesai — `BUILD SUCCESSFUL` |

## Detail

### 1. Project Foundation — ✅ Selesai
- Version catalog (`gradle/libs.versions.toml`)
- `minSdk 26`, `compileSdk`/`targetSdk 35`
- Base classes: `BaseActivity`, `BaseFragment`, `BaseViewModel`, `UiContracts`
- DI dasar: `CoreModule`, `DatabaseModule`
- **Bug yang sempat ditemukan lalu diperbaiki:** `BaseActivity.binding` sempat `protected`, sementara 5 Fragment (`ClientMainFragment`, `EstimatesMainFragment`, `InvoiceMainFragment`, `ItemMainFragment`, `ReportsMainFragment`) mengaksesnya lewat `(activity as MainActivity).binding?.toolbar` dari luar — gagal compile. Fix: `binding` diubah jadi public (1-line visibility change, tanpa ubah behavior lain).

### 2. Offline Database — ✅ Selesai — `BUILD SUCCESSFUL`
- Room entities & DAO: sudah lengkap sejak sebelumnya (Client, Item, PersonalBusiness, Invoice, InvoiceItem, InvoicePhoto, Estimate, EstimateItem, EstimatePhoto)
- Ditambahkan sesi ini:
  - Domain model (`domain/model`), Mapper (`data/mapper`), Repository interface (`domain/repository`) + implementasi (`data/repository`) untuk Client/Item/PersonalBusiness/Invoice/Estimate
  - `RepositoryModule` (Hilt `@Binds`)
  - DAO ditambah query `observeAllX(): Flow<List<X>>` (tanpa hapus method lama)
  - `DataStore` lama (singleton object) dimodernisasi jadi `AppPreferencesDataStore` (Hilt-injectable), ditambah preference `theme_mode` & `currency_code`; 10 Fragment caller sudah dimigrasi, file lama dihapus
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama, tidak terkait Sprint 2)
- ViewModel (Client/Item/Invoice/Estimate/dst) **sengaja belum diubah** untuk pakai Repository — itu scope sprint fitur masing-masing (3–9)

### 3. Business Profile module — ✅ Selesai — `BUILD SUCCESSFUL`
- UseCase baru di `domain/usecase/business/`: `GetBusinessUseCase`, `AddBusinessUseCase`, `UpdateBusinessUseCase`, `DeleteBusinessUseCase`, `SaveBusinessOnboardingUseCase` (upsert-by-name, mirror persis logika lama di flow onboarding splash)
- `SettingViewModel` & `SplashMainViewModel` di-rewire dari akses `AppDataBase`/DAO langsung → lewat UseCase (sesuai hard constraint "Presentation only talks to UseCase, never Room directly")
- Kedua ViewModel sekarang mengekspos `domain.model.PersonalBusiness`, bukan Room entity — `AddBusinessDetailsFragment` & `AddBusinessDetailsSplashFragment` disesuaikan importnya (field/urutan konstruktor identik, tidak ada perubahan behavior)
- Tidak perlu module DI baru — UseCase pakai `@Inject constructor` biasa, di-provide otomatis lewat `PersonalBusinessRepository` yang sudah di-bind di `RepositoryModule` (Sprint 2)
- **Sengaja tidak disentuh:** `InvoiceMainViewModel` & `EstimatesMainViewModel` masih akses `PersonalBusinessDao` langsung (dipakai untuk render profil bisnis di invoice/estimate preview) — itu scope Sprint 6/7, bukan Sprint 3
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL**

### 4. Client module — ✅ Selesai — `BUILD SUCCESSFUL`
- **Temuan saat mulai kerja:** seluruh rewiring Client (UseCase, ViewModel, Fragment, Adapter) ternyata **sudah ada** di codebase sebelum sesi ini dimulai — `domain/usecase/client/` (`GetAllClientsUseCase`, `AddClientUseCase`, `UpdateClientUseCase`, `DeleteClientUseCase`) sudah lengkap, `ClientMainViewModel` sudah 100% pakai UseCase (tidak ada akses `AppDataBase`/`ClientDao` langsung), dan `ClientMainFragment` / `AddClient` (add-edit dialog) / `AllClientsAdapter` semua sudah pakai `domain.model.Client`, bukan Room entity. Tidak diketahui dari sesi mana ini datang — `SPRINTS.md` & memory sebelumnya masih menandai Sprint 4 "Belum mulai", jadi dokumentasi ini out of sync dengan kode aktual.
- **Verifikasi dilakukan sesi ini:** dibaca ulang seluruh rantai Client (`database/models/Client` entity, `domain/model/Client`, `ClientMapper`, `ClientRepository`/`ClientRepositoryImpl`, `ClientDao`, `ClientMainViewModel`, `ClientMainFragment`, `AddClient.kt`, `AllClientsAdapter`) — semua konsisten, tidak ada Room entity bocor ke presentation layer, tidak ada pelanggaran hard constraint.
- **Search & sort:** roadmap awal menyebut scope "CRUD, search, sort", tapi codebase existing **tidak pernah punya fitur search/sort untuk Client** (toolbar search icon selalu `inVisible()`, `popup_menu_client.xml` cuma berisi menu Delete, `ClientDao` tidak ada query pencarian). Karena hard constraint "jangan redesign/tambah fitur baru di luar yang diminta eksplisit" dan "jangan hilangkan logika existing" — tidak ada logika search/sort existing untuk dipertahankan, jadi **tidak ditambahkan** fitur baru di sprint ini. Kalau search/sort untuk Client memang diinginkan, itu perlu diminta eksplisit sebagai task terpisah.
- Tidak ada file baru yang dibuat, tidak ada file yang diubah — kerja sesi ini murni verifikasi + dokumentasi.
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama di file Estimate/Invoice/Setting/Splash, tidak terkait Client)

### 5. Item module — ✅ Selesai — `BUILD SUCCESSFUL`
- **Verifikasi awal:** dicek langsung `ItemMainViewModel` — dikonfirmasi masih akses `AppDataBase`/`ItemDao` langsung (tidak seperti Client di Sprint 4 yang ternyata sudah dimigrasi lebih dulu). Jadi Sprint 5 ini kerja migrasi riil, bukan cuma verifikasi.
- UseCase baru di `domain/usecase/item/`: `GetAllItemsUseCase`, `AddItemUseCase`, `UpdateItemUseCase`, `DeleteItemUseCase` — semua plain `@Inject constructor`, mirror pola persis `domain/usecase/client/` dari Sprint 4.
- `ItemMainViewModel` di-rewire dari `AppDataBase` langsung → 4 UseCase di atas. Nama method (`getAllItems`, `addItem`, `updateItem`, `deleteItem`) dan `itemUpdateModel: MutableLiveData<Item?>` dipertahankan persis agar caller (`ItemMainFragment`, `AddItemFragment`, `AllItemsAdapter`) tidak perlu berubah logikanya, hanya import.
- `ItemMainFragment`, `AddItemFragment`, `AllItemsAdapter` diubah importnya dari `database.models.Item` (Room entity) → `domain.model.Item` (domain model) — field & urutan konstruktor identik (`itemId, itemName, itemUnitCost, itemTaxable, itemDetails`), jadi swap aman tanpa ubah behavior.
- Tidak perlu module DI baru — `ItemRepository` sudah di-bind di `RepositoryModule` sejak Sprint 2.
- **Search & sort:** sama seperti Client di Sprint 4, Item module **tidak pernah punya fitur search/sort** existing (toolbar search selalu `inVisible()`, tidak ada query pencarian di `ItemDao`) — sesuai hard constraint, tidak ditambahkan fitur baru.
- **Sengaja tidak disentuh:** Room entity `database.models.Item` masih dipakai langsung oleh modul lain di luar scope `ui/item` (mis. `ItemDetailForInvoiceFragment`, `ItemDetailForEstimateFragment`, adapter Invoice/Estimate) — itu scope Sprint 6/7, bukan Sprint 5.
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL**

### 6. Invoice module — ✅ Selesai — `BUILD SUCCESSFUL`
- **Verifikasi awal:** dicek langsung `InvoiceMainViewModel` — dikonfirmasi masih inject `AppDataBase` langsung dan memanggil `db.invoiceDao()/invoiceItemDao()/invoicePhotoDao()/personalBusinessDao()/clientDao()/itemDao()` dari banyak method. Modul ini jauh lebih besar dari Client/Item: satu `InvoiceMainViewModel` dipakai bersama (`hiltNavGraphViewModels`) oleh puluhan Fragment sepanjang alur wizard invoice (list, add/edit multi-step, preview, share history, dsb).
- UseCase baru di `domain/usecase/invoice/` (12 file): `ObserveAllInvoicesUseCase`, `AddInvoiceUseCase`, `UpdateInvoiceUseCase`, `DeleteInvoiceUseCase` (cascading: hapus invoice + item + foto, mirror persis logika lama), `GetInvoiceItemsByInvoiceIdUseCase`, `AddInvoiceItemUseCase`, `UpdateInvoiceItemUseCase`, `DeleteInvoiceItemUseCase`, `GetInvoicePhotosByInvoiceIdUseCase`, `AddInvoicePhotoUseCase`, `UpdateInvoicePhotoUseCase`, `DeleteInvoicePhotoUseCase`.
- **Business profile:** `InvoiceMainViewModel` yang sejak Sprint 3 sengaja masih akses `PersonalBusinessDao` langsung untuk embed data profil bisnis di preview, sekarang di-rewire ke `GetBusinessUseCase`/`AddBusinessUseCase`/`UpdateBusinessUseCase` (Sprint 3) — tidak ada lagi akses DAO business langsung.
- **Client & Item list:** `allClients`/`allItems`/`getAllClients()` di-rewire ke `GetAllClientsUseCase` (Sprint 4) & `GetAllItemsUseCase` (Sprint 5).
- **Strategi minim-ripple:** field state ViewModel (`selectedClient`, `selectedItemsList`, `businessUpdateModel`, `allClients`, `allItems`, dst.) TETAP bertipe Room entity (`database.models.*`) — bukan domain model — karena field ini adalah state wizard sementara yang dibaca-tulis oleh ~20 Fragment/Adapter lain (`AllInvoiceAdapter`, `ClientListForInvoiceFragment`, `ItemDetailForInvoiceFragment`, dst). Konversi Room↔domain dilakukan lewat mapper `toDomain()`/`toEntity()` di titik pemanggilan UseCase saja, sehingga tidak ada satupun Fragment/Adapter lain yang perlu diubah — hanya `InvoiceMainViewModel` sendiri yang berubah. Ini konsisten dengan constraint "Presentation only talks to UseCase, never Room directly": ViewModel tidak lagi menyentuh `AppDataBase`/DAO sama sekali, hanya masih memakai tipe entity sebagai bentuk state lokal.
- `allInvoicesLive: LiveData<List<Invoice>>` ditambahkan di ViewModel (`observeAllInvoicesUseCase().map{}.asLiveData()`, tipe Room entity agar `AllInvoiceAdapter` tak berubah) menggantikan `viewModel.db.invoiceDao().getAllInvoicesLive()` yang sebelumnya dipanggil langsung dari 3 Fragment: `AllInvoices.kt`, `OutstandingInvoices.kt`, `PaidInvoices.kt` (satu-satunya titik akses DB langsung di luar ViewModel) — ketiganya diubah jadi `viewModel.allInvoicesLive.observe(...)`.
- **Preserved quirk (didokumentasikan, sengaja tidak "diperbaiki"):** `updateInvoiceItem()` di ViewModel lama sebenarnya memanggil `insertInvoiceItem` DAO (bukan `@Update`), bukan bug — `InvoiceItemDao.insertInvoiceItem` pakai `OnConflictStrategy.REPLACE` sehingga insert-dengan-PK-sama berlaku sebagai upsert. `UpdateInvoiceItemUseCase` baru sengaja mereplikasi perilaku ini persis (bukan memanggil `repository.updateInvoiceItem` yang benar-benar berbeda perilakunya) — didokumentasikan di KDoc use case tersebut agar tidak dikira bug oleh sprint berikutnya.
- Tidak perlu module DI baru — semua Repository binding sudah ada sejak Sprint 2.
- **Tidak ditambahkan (di luar scope, tidak ada implementasi existing):** fitur "duplicate invoice" tidak pernah ada di codebase (`grep -i duplicate` kosong); status invoice hanya `PAID`/`UN_PAID` (`InvoiceStatusEnum`), tidak ada `Draft`/`Outstanding`/`Cancelled` — sesuai hard constraint anti-fitur-baru/anti-redesign, tidak ditambahkan.
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama, tidak terkait Sprint 6)

### 7. Estimate module — ✅ Selesai — `BUILD SUCCESSFUL`
- **Verifikasi awal:** dicek langsung `EstimatesMainViewModel` — dikonfirmasi masih inject `AppDataBase` langsung, memanggil `db.estimateDao()/estimateItemDao()/estimatePhotoDao()/personalBusinessDao()/clientDao()/itemDao()`. Strukturnya nyaris identik dengan `InvoiceMainViewModel` pra-Sprint-6 (shared ViewModel via `hiltNavGraphViewModels`, dipakai banyak Fragment wizard add/edit/preview).
- UseCase baru di `domain/usecase/estimate/` (12 file, mirror 1:1 pola Sprint 6): `ObserveAllEstimatesUseCase`, `AddEstimateUseCase`, `UpdateEstimateUseCase`, `DeleteEstimateUseCase` (cascading: hapus estimate + item + foto), `GetEstimateItemsByEstimateIdUseCase`, `AddEstimateItemUseCase`, `UpdateEstimateItemUseCase`, `DeleteEstimateItemUseCase`, `GetEstimatePhotosByEstimateIdUseCase`, `AddEstimatePhotoUseCase`, `UpdateEstimatePhotoUseCase`, `DeleteEstimatePhotoUseCase`.
- **Business profile & Client/Item list:** akses `PersonalBusinessDao` langsung dirapikan ke `GetBusinessUseCase`/`UpdateBusinessUseCase` (Sprint 3); `allClients`/`allItems` di-rewire ke `GetAllClientsUseCase` (Sprint 4) & `GetAllItemsUseCase` (Sprint 5).
- **Strategi minim-ripple (sama seperti Sprint 6):** field state ViewModel (`selectedClient`, `selectedItemsList`, `businessUpdateModel`, `allClients`, `allItems`, dst) TETAP bertipe Room entity — bukan domain model — karena dibaca-tulis oleh banyak Fragment/Adapter lain (`AllEstimateAdapter`, `ClientListForEstimateFragment`, `ItemListForEstimate`, dst). Konversi Room↔domain terjadi lewat mapper `toDomain()`/`toEntity()` hanya di titik panggil UseCase, jadi tidak ada Fragment/Adapter lain yang perlu diubah selain `EstimatesMainViewModel` sendiri.
- `allEstimatesLive: LiveData<List<Estimate>>` ditambahkan di ViewModel menggantikan `viewModel.db.estimateDao().getAllEstimatesLive()` yang sebelumnya dipanggil langsung dari 3 Fragment: `AllEstimatesFragment.kt`, `OpenEstimatesFragment.kt`, `ClosedEstimatesFragment.kt` — satu-satunya titik akses DB langsung di luar ViewModel, sekarang jadi `viewModel.allEstimatesLive.observe(...)`.
- **Preserved quirk (didokumentasikan, sengaja tidak "diperbaiki"):** `updateEstimateItem()` ViewModel lama memanggil `insertEstimateItem` DAO (`OnConflictStrategy.REPLACE`, upsert-by-PK), bukan `@Update` — persis pola `UpdateInvoiceItemUseCase` di Sprint 6. `UpdateEstimateItemUseCase` baru mereplikasi ini persis, didokumentasikan di KDoc.
- Tidak perlu module DI baru — semua Repository binding sudah ada sejak Sprint 2.
- **Tidak ditambahkan (di luar scope, tidak ada implementasi existing):** fitur "convert to invoice" yang disebut roadmap awal TIDAK PERNAH ada di codebase (`grep -i convert` di seluruh source kosong, tidak ada menu/tombol terkait) — sesuai hard constraint anti-fitur-baru, tidak ditambahkan. Kalau memang diinginkan, perlu diminta eksplisit sebagai task terpisah.
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama di `AddEstimateMainFragment`/`AddPhotoToEstimateFragment`/`EditBusinessDetailsFromEstimateFragment`, tidak terkait Sprint 7)

### 8. Dashboard — ✅ Selesai — `BUILD SUCCESSFUL`
Sesi sebelumnya menemukan Dashboard belum pernah dibangun (cuma scaffolding kosong, bottom nav aktual = Invoices/Estimates/Client/Item/Reports, bukan sesuai roadmap). User dimintai konfirmasi eksplisit lewat pertanyaan langsung dan memilih **"Bangun penuh sesuai spec awal"** — sesi ini mengeksekusi keputusan tersebut, bukan asumsi sepihak.

**Dibangun:**
- `domain/usecase/dashboard/GetDashboardSummaryUseCase.kt` (baru) — agregasi lintas `InvoiceRepository`+`EstimateRepository` (sudah ada sejak Sprint 2), hitung total invoice, revenue paid (`InvoiceStatusEnum.PAID`), total & jumlah outstanding (`InvoiceStatusEnum.UN_PAID`), jumlah estimate open (`EstimateStatusEnum.OPEN`). Expose `Flow<DashboardSummary>`.
- `ui/dashboard/DashboardViewModel.kt` (rewrite total, `@HiltViewModel`) — pakai `GetDashboardSummaryUseCase` + `ObserveAllInvoicesUseCase` (Sprint 6, reuse langsung, tidak duplikat) untuk recent invoice (sort by ID desc, take 5). Scoped biasa (`by viewModels()`), bukan `hiltNavGraphViewModels`, karena Dashboard bukan wizard multi-fragment seperti Invoice/Estimate.
- `ui/dashboard/DashboardFragment.kt` (rewrite total, hapus kode test `SignatureView`) + `ui/dashboard/adapter/RecentInvoiceAdapter.kt` (baru, reuse `ItemClientBinding`/`item_client.xml` seperti pola `AllInvoiceAdapter` Sprint 6) + `res/layout/fragment_dashboard.xml` (rewrite total): 4 `MaterialCardView` summary stat (Total Invoices, Revenue Paid, Outstanding, Open Estimates, warna dari role M3 container yang sudah ada di `colors.xml`), RecyclerView Recent Invoices, `FloatingActionButton` quick-action (popup menu: New Invoice → navigate `invoice_navigation_graph`, New Estimate → navigate `estimate_navigation_graph` — TIDAK menduplikasi logika `insertInvoice` yang fragile di `InvoiceMainFragment`, cukup arahkan ke tab yang benar).
- `ui/more/MoreFragment.kt` + `res/layout/fragment_more.xml` (baru) — 2 `MaterialCardView` shortcut ke Client & Item, reuse `client_navigation_graph`/`item_navigation_graph` yang sudah ada (Fragment/ViewModel/UseCase Client & Item dari Sprint 4/5 **tidak disentuh sama sekali**, hanya entry point navigasinya pindah).
- `res/navigation/dashboard_navigation_graph.xml`, `res/navigation/more_navigation_graph.xml` (baru) — masing-masing 1 fragment sederhana, di-`<include>` ke `mobile_navigation.xml`.
- `res/menu/bottom_nav_menu.xml` — diubah jadi 5 item: **Dashboard, Invoice, Estimate, Reports, More** (urutan sesuai roadmap). Icon Dashboard reuse `ic_dashboard_black_24dp.xml` yang sudah ada (leftover tapi valid); icon More baru `ic_more_nav.xml` (3-dot MD3-style, tint mengikuti selector `tab_icon` yang sudah ada, sama seperti icon lain).
- **Splash/PIN-unlock flow diarahkan ulang ke Dashboard** (dulu ke `invoice_navigation_graph`, sekarang jadi home yang benar): `SplashMainFragment.kt`, `AddBusinessDetailsSplashFragment.kt`, `EnterPasswordFragment.kt`, `SavePasswordFragment.kt` — masing-masing 1 baris `navigate(R.id.invoice_navigation_graph)` → `navigate(R.id.dashboard_navigation_graph)`. Ini konsisten dengan bottom nav yang sekarang menempatkan Dashboard sebagai tab pertama.
- Strings baru ditambahkan ke `strings.xml` (title_more, label_*, action_*, more_menu_*) — tidak ada string baru yang di-hardcode di XML.
- **Tidak disentuh:** `setting_navigation_graph.xml` (Settings, diakses lewat ikon gear toolbar) tetap terpisah dari "More" — sesuai instruksi, tidak digabung tanpa alasan UX jelas.
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama, tidak terkait Sprint 8). UI belum diverifikasi visual langsung di emulator/device — hanya diverifikasi lewat compile, karena tidak ada emulator/device terhubung di sesi ini.

### 9. Reports — ✅ Selesai — `BUILD SUCCESSFUL`
- **Verifikasi awal:** dicek langsung `ReportsMainViewModel` — dikonfirmasi masih inject `AppDataBase` langsung dan memanggil `db.invoiceDao().getAllInvoices()`/`getAllPaidInvoices()`. UI Reports (`ReportsMainFragment` + 2 tab: `ClientsReportFragment`, `PaidReportFragment`, shared ViewModel via `hiltNavGraphViewModels`) sudah ada dan fungsional — jadi ini migrasi ringan seperti Sprint 4/5, bukan build-dari-nol seperti Sprint 8.
- UseCase baru di `domain/usecase/reports/`: `GetAllInvoicesForReportUseCase`, `GetAllPaidInvoicesForReportUseCase` — masing-masing plain `@Inject constructor` yang membungkus `InvoiceRepository.getAllInvoices()`/`getAllPaidInvoices()` (kedua method repository ini sudah ada sejak Sprint 2, tidak perlu diubah).
- `ReportsMainViewModel` di-rewire dari `AppDataBase` langsung → 2 UseCase di atas. Method (`getClientReport()`, `getPaidClientReport()`) dan `data class ClientReportModel` dipertahankan persis (termasuk perilaku append-ke-ArrayList tanpa clear, yang merupakan quirk lama — bukan bug yang diperbaiki di sini) — `ClientsReportFragment`/`PaidReportFragment`/`ClientsAdapter` tidak perlu diubah sama sekali karena tidak pernah menyentuh Room entity secara langsung.
- Tidak perlu module DI baru — `InvoiceRepository` sudah di-bind di `RepositoryModule` sejak Sprint 2.
- **Tidak ditambahkan (di luar scope, tidak ada implementasi existing):** roadmap awal menyebut scope "revenue/monthly/outstanding/paid breakdown, pie/bar chart, filtering", tapi kenyataan kode hanya berupa 2 RecyclerView list sederhana (tab "Paid" & "Clients") tanpa chart apapun — cek `gradle/libs.versions.toml`/`build.gradle.kts` mengonfirmasi **tidak ada dependency chart library** (mis. MPAndroidChart) di project ini sama sekali, dan tidak ada breakdown bulanan atau UI filtering. Sesuai hard constraint anti-fitur-baru/anti-redesign (pola sama seperti gap search/sort di Sprint 4/5, duplicate invoice di Sprint 6, convert-to-invoice di Sprint 7), fitur-fitur ini **tidak ditambahkan** — kalau memang diinginkan, perlu diminta eksplisit sebagai task terpisah (bukan bagian dari migrasi arsitektur).
- `./gradlew :app:compileDebugKotlin` → **BUILD SUCCESSFUL** (sisa cuma warning deprecation lama tidak terkait, termasuk 2 warning lama di `ReportsMainFragment.kt` baris 74-75 yang sudah ada sebelum sprint ini)

## Roadmap 9-Sprint: Selesai

Seluruh 9 sprint modernisasi arsitektur (Foundation → Offline Database → Business Profile → Client → Item → Invoice → Estimate → Dashboard → Reports) sudah selesai dan setiap sprint diverifikasi `BUILD SUCCESSFUL` secara independen. Catatan status akhir:
- Semua Presentation layer (ViewModel) di modul Business/Client/Item/Invoice/Estimate/Dashboard/Reports sudah 100% lewat UseCase → Repository (expose Flow), tidak ada lagi akses `AppDataBase`/DAO langsung dari ViewModel manapun.
- Beberapa fitur yang disebut di roadmap awal ternyata **tidak pernah ada** di implementasi asli dan sesuai hard constraint tidak ditambahkan sebagai bagian dari modernisasi arsitektur ini: search/sort Client & Item (Sprint 4/5), duplicate invoice & status Draft/Outstanding/Cancelled (Sprint 6, status riil hanya PAID/UN_PAID), convert-to-invoice dari Estimate (Sprint 7), pie/bar chart & monthly/filtering di Reports (Sprint 9). Semua ini butuh keputusan/permintaan eksplisit terpisah kalau memang diinginkan sebagai fitur baru.
- Satu-satunya sprint yang genuinely membangun UI/struktur baru (bukan migrasi arsitektur) adalah Sprint 8 (Dashboard + More + restrukturisasi bottom nav ke 5 item: Dashboard/Invoice/Estimate/Reports/More), dikerjakan setelah konfirmasi eksplisit user. UI hasil Sprint 8 baru diverifikasi lewat compile, **belum divisualkan manual di emulator/device**.
- Repo masih belum di-`git init` — tidak ada riwayat commit, semua perubahan hanya ada di working tree.

## Next step yang disarankan
Roadmap arsitektur selesai. Kandidat langkah lanjutan (butuh keputusan/permintaan eksplisit user, bukan otomatis): (1) verifikasi visual UI Dashboard/More di emulator atau device, (2) putuskan apakah fitur-fitur gap di atas (search/sort, duplicate invoice, convert-to-invoice, chart Reports) memang diinginkan sebagai pekerjaan baru, (3) pertimbangkan `git init` untuk mulai punya riwayat versi.
