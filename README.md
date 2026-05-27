# commonlib 사용 가이드

안드로이드 앱 공통 기반(베이스 액티비티/프래그먼트, 타이틀바, Prefs, 권한, 확장함수)을 제공하는 라이브러리.

## 설치 (jitpack)
```gradle
// settings.gradle 또는 build.gradle
repositories { maven { url 'https://jitpack.io' } }

// app/build.gradle
dependencies {
    implementation 'com.github.ghost1236.commomLib:commonlib:2.0.1'
}
```
> ⚠️ **Kotlin 2.1.20 / AGP 8.6 / compileSdk 35** 로 빌드됨 → 소비 앱도 **Kotlin 2.1+, compileSdk 35** 필요.
> 베이스 액티비티/프래그먼트가 **DataBinding** 을 사용하므로, 쓰는 앱은 `buildFeatures { dataBinding true }` 와 `<layout>` 래핑 XML 이 필요합니다.
> 정확한 좌표는 jitpack 페이지(`jitpack.io/#ghost1236/commomLib`)에서 확인.

---

## 제공 컴포넌트
| 분류 | 항목 |
|---|---|
| 베이스 | `BaseActivity`, `BaseFragment`, `BaseFragmentActivity`, `TitlebarActivity` |
| 뷰 | `BaseTitlebar`, `AutoResizeTextView` |
| 유틸 | `Prefs`, `PermissionUtils`, `NotificationUtils`, `JsonParser`, `Constants` |
| 확장함수 | `String`/`Json`/`Logger`/`Map`/`Cursor`/`ByteArray`/`Float`/`Int`/`ImageView`/`Activity` Extension |

---

## 1. BaseActivity 상속 (DataBinding)
```kotlin
class MainActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_main   // <layout> 으로 래핑된 XML
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)               // DataBindingUtil.setContentView 자동
        val binding = mBinding as ActivityMainBinding     // 생성된 바인딩으로 캐스팅
        // ...
    }
}
```
- `getLayoutId()` 만 구현하면 `super.onCreate` 가 `setContentView` + DataBinding 을 처리
- 화면 전환 헬퍼 제공: `startActivity(intent, enterAnim, exitAnim, isFinish)`, `startActivityResultLauncher(...)`, `finishActivity(...)`

## 2. BaseFragment
```kotlin
class MyFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_my
    // onCreateView 에서 DataBinding inflate, mBinding/mView 제공
}
```

## 3. BaseFragmentActivity (프래그먼트 컨테이너)
```kotlin
class HostActivity : BaseFragmentActivity() {
    override fun getLayoutId() = R.layout.activity_host
    override fun getFragmentView(tag: String): Fragment = when (tag) { ... }
}
// 프래그먼트 추가/교체/제거 헬퍼
AddFragment(this, R.id.container, "home", backstack = false)
replaceFragment(this, R.id.container, "detail", bundle, backstack = true)
RemoveViewTag(this, "detail")
```

## 4. TitlebarActivity (타이틀바 포함)
```kotlin
class MyActivity : TitlebarActivity() {
    override fun getLayoutId() = R.layout.activity_my
    override fun setTitlebar(): BaseTitlebar = /* 타이틀바 구성 */
    override fun leftClickEvent() { finish() }    // 좌측 버튼
    override fun rightClickEvent() { /* ... */ }  // 우측 버튼
}
```

## 5. Prefs (SharedPreferences 래퍼)
```kotlin
Prefs.putString(context, "token", "abc")
val token = Prefs.getString(context, "token", "")

Prefs.putBoolean / getBoolean
Prefs.putInt / getInt
Prefs.putLong / getLong
Prefs.putMap(context, "data", map)            // Map<String, Any>
Prefs.getArrayList(context, "list")           // ArrayList<Map<String, Any>>
Prefs.remove(context, "token")
Prefs.clear(context)
```

## 6. PermissionUtils (런타임 권한)
```kotlin
class AppPermissions : PermissionUtils() {
    override fun setEssentialList() = arrayOf(Manifest.permission.CAMERA)
    override fun setOptionalList() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
}

val perm = AppPermissions()
if (!perm.checkEssential(activity)) {
    perm.requestPermissions(activity, perm.getEssentialDeniedList(activity).toTypedArray(), REQ_CODE)
}
```

## 7. 확장함수 / 기타
`String`/`Json`/`Logger`/`Map` 등 확장함수와 `NotificationUtils`, `AutoResizeTextView`, `JsonParser` 를 제공합니다. (각 클래스의 메서드는 소스 참조)

---

## 참고
- 베이스 액티비티/프래그먼트는 DataBinding 전제 → 레이아웃 XML 을 `<layout> ... </layout>` 으로 래핑해야 합니다.
- `BaseActivity.mBinding`/`activity` 는 companion 의 공유 참조이므로, 동시에 여러 화면에서 접근할 때는 주의하세요.
