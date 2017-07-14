[English Version](https://github.com/tpay-com/Android-SDK/blob/master/readme.md#english-version)

### Tpay Android Mobile Library
Biblioteka mobilna przygotowana dla systemu Android.

### Konfiguracja projektu
* Biblioteka jest zgodna z API >= 16. Jest to minimalna wspierana wersja, którą należy ustawić w projekcie.
* W folderze "app", znajdującym się w głównym katalogu projektu, należy dodać nowy folder o nazwie "libs".
* Do folderu "libs" należy przekopiować bibliotekę "tpay-android-library.aar".
* W pliku "build.gradle" (należącym do głównego modułu projektu, w przykładowym projekcie jest to "app") należy dodać: 

```
repositories {
flatDir {
dirs 'libs'
}
}
```
oraz w sekcji "dependencies":

```
dependencies {
compile(name:'tpay-android-library', ext:'aar')
compile 'com.squareup.retrofit2:retrofit:2.3.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
}
```

* Ostatnim etapem integracji jest sychronizacja projektu z dodaną biblioteką poprzez naciśnięcie przycisku "Sync Project with Gradle Files" (zielona ikona w górnej części interfejsu Android Studio).
* Biblioteka jest zależna od pakietów "com.squareup.retrofit2:retrofit:2.3.0" i "com.squareup.retrofit2:converter-gson:2.1.0" których użycie powinno być zadeklarowane w pliku *build.gradle* jak pokazano powyżej.

### Sposób użycia biblioteki w projekcie - płatności poprzez mobilną stronę WWW
Poniżej opisano przykładowy sposób rozszerzenia klasy Activity.

- Po poprawnym skonfigurowaniu projektu rozpocznij od deklaracji zmiennej:

```
private TpayPayment.Builder paymentBuilder = null;
```

- W metodzie onSaveInstanceState dodaj następujący fragment kodu:

```
@Override
protected void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);
outState.putParcelable(
TpayActivity.EXTRA_TPAY_PAYMENT, 
paymentBuilder);
}
```

- W metodzie onCreate dodaj:

```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
// ustawienia parametrów
} else {
paymentBuilder = savedInstanceState
.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```

- Po utworzeniu obiektu reprezentującego płatność należy ustawić wymagane paramatry zgodnie z
dokumentacją znajdującą się na stronie tpay.com ([dokumentacja](https://secure.tpay.com/partner/pliki/dokumentacja.pdf)). Przykładowe ustawienia parametrów:
```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
.setId("twoje_id")
.setAmount("kwota_transakcji")
.setCrc("crc")
.setSecurityCode("kod_bezpieczeństwa")
.setDescription("opis_transakcji")
.setClientEmail("email_klienta")
.setClientName("imie_nazwisko_klienta");
} else {
paymentBuilder = savedInstanceState
.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```

- Zamiast podawania parametrów "security code" i "crc", można podać parametr "md5 code", który wygenerować można zgodnie z [dokumentacją](https://secure.tpay.com/partner/pliki/dokumentacja.pdf)):

```    
paymentBuilder = new TpayPayment.Builder()
.setMd5Code("wygenerowany_kod_MD5")
```

- Można również ustawić gotowy, wygenerowany wcześniej link. Konfiguracja obiektu
reprezentującego płatność wygląda wtedy następująco:

```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
.setPaymentLink("wygenerowany_link_platnosci");
} else {
paymentBuilder = savedInstanceState.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```

- Aby rozpocząć proces płatności, wywołujemy aktywność oczekującą na rezultat poprzez dodanie poniższego fragmentu kodu w odpowiednim miejscu (np. kod może być wywoływany po naciśnięciu przycisku):

```
final Intent payIntent = new Intent(MainActivity.this,
TpayActivity.class);
final TpayPayment tpayPayment = paymentBuilder.create();
payIntent.putExtra(TpayActivity.EXTRA_TPAY_PAYMENT, tpayPayment);

startActivityForResult(payIntent, 
TpayActivity.TPAY_PAYMENT_REQUEST);
```

Po wywołaniu aktywności, aplikacja pokaże widok WebView, dzięki któremu można będzie przejść przez cały proces płatności.

* Ostatni krok to dodanie poniższego kodu, obsługującego informacje zwrotne z wywołanej w poprzednim korku aktywności, w metodzie onActivityResult:

```
switch (requestCode) {
case TpayActivity.TPAY_PAYMENT_REQUEST:
if (resultCode == RESULT_OK) {
// Transakcja poprawna. Poczekaj na powiadomienie.    
} else {
// Użytkownik anulował transakcję lub wystąpił błąd.
}
default:
super.onActivityResult(requestCode, resultCode, data);
}
```

### Sposób użycia biblioteki w projekcie - płatności BLIK oraz BLIK OneClick
#### Użycie domyślnych widoków

Biblioteka pozwala na szybkie użycie płatności BLIK oraz BLIK One Click za pomocą gotowych, domyślnych widoków płatności.

W pierwszym kroku należy stworzyć obiekt reprezentujący transakcję BLIK, wykorzystując do tego przygotowany builder:

```
TpayBlikTransaction transaction = new TpayBlikTransactionBuilder()
.setApiPassword("haslo_api")
.setId("twoje_id")
.setAmount("kwota_transakcji")
.setCrc("kod_crc")
.setSecurityCode("kod_bezpieczenstwa")
.setDescription("opis_transakcji")
.setClientEmail("email_klienta")
.setClientName("imie_nazwisko_klienta")
.addBlikAlias("alias_blik", "etykieta", "klucz_aplikacji")
.build();
```

Hasło do api (parametr *api_password*) jest polem obowiązkowym - w [dokumentacji API ](https://secure.tpay.com/partner/pliki/api-transaction.pdf) na stronie 2. można znaleźć więcej szczegółów.  Pozostałe parametry opisane są w [dokumentacji ogólnej](https://secure.tpay.com/partner/pliki/dokumentacja.pdf). 

Zamiast podawania parametrów *security code* i *crc*, można podać parametr *md5 code*, który wygenerować można zgodnie z [dokumentacją](https://secure.tpay.com/partner/pliki/dokumentacja.pdf).

W przypadku transakcji BLIK bez możliwości rejestracji aliasu (czyli bez możliwości skorzystania z One Click) dodanie aliasu BLIK jest opcjonalne. W przypadku transakcji dla zarejestrowanego aliasu, bądź chęci rejestracji aliasu należy podać przynajmniej jeden alias za pomocą metody *addBlikAlias()*. 

Metoda *addBlikAlias()* przyjmuje parametry:
- alias: pole obowiązkowe, typ String
- label: etykieta aliasu, pole opcjonalne, typ String
- key: numer aplikacji, pole opcjonalne, typ String.

Więcej informacji na temat poszczególnych parametrów zawarto w [dokumentacji API ](https://secure.tpay.com/partner/pliki/api-transaction.pdf) na stronie 7.

Jeden alias BLIK może być zarejestrowany do wielu aplikacji bankowych, co powoduje niejednoznaczność aliasu - domyślny widok płatności obsługuje tę sytuację wyświetlając stosowny widok wyboru.

Kolejnym krokiem, pozwalającym na wyświetlenie domyślnego widoku płatności, jest przygotowanie intencji za pomocą statycznej metody *createIntent()* klasy TpayBlikDefaultActivity:

```
Intent intent = TpayBlikDefaultActivity.createIntent(activity,
transaction,
key,
viewType);
```

Metoda *createIntent()* przyjmuje parametry:
- activity - referencja do aktywności, z której przechodzić będziemy do domyślnego widoku płatności
- transaction - obiekt transakcji stworzony w kroku 1.
- key - unikalny ciąg dostępu, wygenerowany w Panelu Odbiorcy Płatności w zakładce
Ustawienia->API
- viewType: jedna z wartości TpayBlikDefaultActivity.BlikViewType.REGISTERED_ALIAS, TpayBlikDefaultActivity.BlikViewType.UNREGISTERED_ALIAS, TpayBlikDefaultActivity.BlikViewType.ONLY_BLIK.

Typ widoku, który powiniśmy wybrać zależny jest od typu transakcji, którą chcemy przeprowadzić:
- TpayBlikDefaultActivity.BlikViewType.ONLY_BLIK - pokazuje widok pozwalający dokonać jedynie transakcji BLIK, bez możliwości rejestracji aliasu (dodawanie aliasu do obiektu transakcji nie jest wtedy konieczne)
- TpayBlikDefaultActivity.BlikViewType.UNREGISTERED_ALIAS - pokazuje widok pozwalający dokonać transakcji BLIK z możliwością wyrażenia chęci rejestracji aliasu
- TpayBlikDefaultActivity.BlikViewType.REGISTERED_ALIAS - pokazuje widok płatności dla zarejestrowanego aliasu.

Ponadto dostępny jest również typ TpayBlikDefaultActivity.BlikViewType.NON_UNIQUE_ALIAS, używany wewnątrz biblioteki do obsługi sytuacji niejednoznaczego aliasu BLIK.

Następnie należy wywołać aktywność oczekującą na rezultat zgodnie z przygotowaną intencją:

```
startActivityForResult(intent,
TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST);
```


Ostatni krok to dodanie poniższego kodu, obsługującego informacje zwrotne z wywołanej w poprzednim korku aktywności, w metodzie onActivityResult:


```
if (requestCode == TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST) {
switch (resultCode) {
case TpayBlikDefaultActivity.BLIK_RESULT_SUCCESS:
// Transakcja poprawna. 
// Klient powinien zatwierdzić płatność
// w aplikacji mobilnej banku. 
// Poczekaj na powiadomienie.    
break;

case TpayBlikDefaultActivity.BLIK_RESULT_ERROR:
// Wystąpił błąd o podanym kodzie błędu.
// Więcej w dokumentacji API.
break;

case TpayBlikDefaultActivity.BLIK_RESULT_FAILURE:
// Wystąpił błąd typu Throwable,
// np. brak połączenia internetowego.
break;
}
}
```

#### Samodzielna obsługa płatności BLIK i BLIK One Click
Biblioteka zawiera metody pozwalające na obsługę płatności bez wykorzystania domyślnych widoków. 

Należy stworzyć obiekt reprezentujący transakcję BLIK:

```
TpayBlikTransaction transaction = new TpayBlikTransactionBuilder()
.setApiPassword("haslo_api")
.setId("twoje_id")
.setAmount("kwota_transakcji")
.setCrc("kod_crc")
.setSecurityCode("kod_bezpieczenstwa")
.setDescription("opis_transakcji")
.setClientEmail("email_klienta")
.setClientName("imie_nazwisko_klienta")
.setBlikCode("6_cyfrowy_kod_blik")
.addBlikAlias("alias_blik", "etykieta", "klucz_aplikacji")
.build();
```

Szczegółowy opis w sekcji *Użycie domyślnych widoków*.

Następnie należy skorzystać z klienta pozwalającego na wysłanie transakcji oraz obsłużyć odpowiedzi:

```
TpayClient.getInstance().postBlikTransaction(key, transaction, 
new TpayBlikCallback<TPayBlikResponse>() {
@Override
public void onResponseSuccess(TPayBlikResponse response) {
// Sukces oznacza jedynie, 
// że transakcja została wysłana poprawnie.
// Rezultat zależny od otrzymanej odpowiedzi.
}

@Override
public void onResponseError(ResponseBody errorBody) {
// Błąd w trakcie wysyłania transakcji.
}

@Override
public void onResponseFailure(Throwable t) {
// Błąd w trakcie wysyłania transakcji typu Throwable.
// Np. brak połączenia z internetem.
}
});
```

Szczegóły związane z odpowiedziami API oraz kodami błędów znajdują się w [dokumentacji API ](https://secure.tpay.com/partner/pliki/api-transaction.pdf) na stronach 6-13.

Historia zmian
--------------

Wersja 1.0 (Czerwiec 2015)
Wersja 2.0 (Maj 2017)
Wersja 3.0 (Lipiec 2017)

### English version

### Tpay Android Mobile Library
Mobile Library prepared for Android.

### Configuration of the project
* The library is compliant with API >= 16. This is the minimum supported version that should be set in the project.
* In the "app" folder in the main project catalogue, add a new folder named "libs".
* Copy the "tpay-android-library.aar" library to the "libs" folder.
* In the "build.gradle" file (belonging to the main project module, in the example project it is "app"), add:
```
repositories {
flatDir {
dirs 'libs'
}
}
```
and in the “dependencies” section:
```
dependencies {
compile(name:'tpay-android-library', ext:'aar')
compile 'com.squareup.retrofit2:retrofit:2.3.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
}
```
* The last stage of integration is the sycronization of the project with the added library by pressing the "Sync Project with Gradle Files" button (green icon at the top of the Android Studio interface).
* The library is dependent on packages "com.squareup.retrofit2: retrofit: 2.3.0" and "com.squareup.retrofit2: converter-gson: 2.1.0" whose use should be declared in the build.gradle file as shown above.

### How to use the library in the project - payment via mobile web page
The following is an example of how to extend the Activity class.

- After you have properly configured the project, start with the variable declaration:
```
private TpayPayment.Builder paymentBuilder = null;
```

- In the onSaveInstanceState method, add the following code snippet:
```
@Override
protected void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);
outState.putParcelable(
TpayActivity.EXTRA_TPAY_PAYMENT, 
paymentBuilder);
}
```

- In the onCreate method, add:
```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
// parameter settings
} else {
paymentBuilder = savedInstanceState
.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```
- After creating a payment object, set the required parameters according to the documentation on tpay.com (documentation). Sample parameter settings:
```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
.setId("your_id")
.setAmount("transactions_amount")
.setCrc("crc")
.setSecurityCode("security_code")
.setDescription("transaction_description")
.setClientEmail("customer_email")
.setClientName("customer_name_surname");
} else {
paymentBuilder = savedInstanceState
.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```

- Instead of providing "security code" and "crc" parameters, you can specify "md5 code", which can be generated according to the documentation):
```
paymentBuilder = new TpayPayment.Builder()
.setMd5Code("generated_code_MD5")
```

- You can also set up a pre-generated link. In this case, the configuration of the payment object looks like this:
```
if (savedInstanceState == null) {
paymentBuilder = new TpayPayment.Builder()
.setPaymentLink("generated_payment_link");
} else {
paymentBuilder = savedInstanceState.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
}
```
- To initiate the payment process, call the activity waiting for the result by adding the following code snippet to the appropriate place (e.g., the code can be called by pushing a button):
```
final Intent payIntent = new Intent(MainActivity.this,
TpayActivity.class);
final TpayPayment tpayPayment = paymentBuilder.create();
payIntent.putExtra(TpayActivity.EXTRA_TPAY_PAYMENT, tpayPayment);

startActivityForResult(payIntent, 
TpayActivity.TPAY_PAYMENT_REQUEST);
```
Once the activity is called, the application will show a WebView view that will allow you to navigate through the entire payment process.

* The last step is to add the following code, which supports feedback from the activity in the previous step, in the onActivityResult method:
```
switch (requestCode) {
case TpayActivity.TPAY_PAYMENT_REQUEST:
if (resultCode == RESULT_OK) {
// Transaction correct. Wait for the notification.    
} else {
// User canceled the transaction or an error occurred.
}
default:
super.onActivityResult(requestCode, resultCode, data);
}
```
### How to use the library in the project - BLIK and BLIK OneClick payments
### The use of default views
The library allows you to quickly use BLIK and BLIK One Click payments using the ready, default payment views.

In the first step, create an object representing the BLIK transaction using the prepared builder:
```
TpayBlikTransaction transaction = new TpayBlikTransactionBuilder()
.setApiPassword("api_password")
.setId("your_id")
.setAmount("transaction_amount")
.setCrc("crc_code")
.setSecurityCode("security_code")
.setDescription("transaction_description")
.setClientEmail("customer_email")
.setClientName("customer_name_surname")
.addBlikAlias("alias_blik", "label", "application_key")
.build();
```

The api password (api_password parameter) is a mandatory field - see API documentation on page 2 for more details. Other parameters are described in the general documentation.
Instead of providing security code and crc parameters, you can specify the md5 code parameter that can be generated according to the documentation.

In the case of BLIK transactions without the ability to register an alias (ie without the ability to use One Click), adding a BLIK alias is optional. In the case of transactions for a registered alias or wanting to register an alias, you must specify at least one alias using the addBlikAlias() method.

The *addBlikAlias()* method takes the following parameters:
- alias: mandatory field, String type
- label: alias label, optional field, String type
- key: application number, optional field, String type.

For more information on individual parameters, see the API documentation on page 7.

One BLIK alias can be registered to multiple banking applications, resulting in alias ambiguity - the default payment view handles this situation by displaying the corresponding selection view.

The next step to display the default payment view is to prepare the intent with the static *createIntent()* method of the TpayBlikDefaultActivity class:
```
Intent intent = TpayBlikDefaultActivity.createIntent(activity,
transaction,
key,
viewType);
```
The *createIntent()* method takes the following parameters:
- activity - a reference to the activity from which we go to the default payment view
- transaction - transaction object created in step 1.
- key - unique access string, generated in the Merchant Panel in the Settings-> API tab
 - viewType: one of the values TpayBlikDefaultActivity.BlikViewType.REGISTERED_ALIAS, TpayBlikDefaultActivity.BlikViewType.UNREGISTERED_ALIAS, TpayBlikDefaultActivity.BlikViewType.ONLY_BLIK.

The type of view that we should choose depends on the type of transaction we want to perform:
- TpayBlikDefaultActivity.BlikViewType.ONLY_BLIK - shows a view allowing only BLIK transactions, without the ability to register an alias (adding an alias to the transaction object is not necessary)
- TpayBlikDefaultActivity.BlikViewType.UNREGISTERED_ALIAS - shows a view allowing BLIK transaction with the ability to express the desire to register an alias
- TpayBlikDefaultActivity.BlikViewType.REGISTERED_ALIAS - shows a payment view for the registered alias.

In addition, the type TpayBlikDefaultActivity.BlikViewType.NON_UNIQUE_ALIAS is also available, which is used inside a library to handle ambiguous BLIK aliases.

Then, the activity waiting for the result according to the prepared intent should be called:
```
startActivityForResult(intent,
TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST);
```

The last step is to add the following code, which supports feedback from the activity in the previous call, in the onActivityResult method:
```
if (requestCode == TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST) {
switch (resultCode) {
case TpayBlikDefaultActivity.BLIK_RESULT_SUCCESS:
// Correct transaction.  
// The customer should approve the payment
// in the bank's mobile application. 
// Wait for the notification.    
break;

case TpayBlikDefaultActivity.BLIK_RESULT_ERROR:
// An error occurred with the specified error code.
// More in API documentation.
break;

case TpayBlikDefaultActivity.BLIK_RESULT_FAILURE:
// Throwable error occurred,
// e.g. no internet connection.
break;
}
}
```

### Self-service of BLIK and BLIK One Click payments
The library provides methods for handling payments without using default views.

Create an object representing the BLIK transaction:
```
TpayBlikTransaction transaction = new TpayBlikTransactionBuilder()
.setApiPassword("api_password")
.setId("your_id")
.setAmount("transaction_amount")
.setCrc("crc_code")
.setSecurityCode("security_code")
.setDescription("transaction_description")
.setClientEmail("customer_email")
.setClientName("customer_name_surname")
.setBlikCode("6_digit_blik_code")
.addBlikAlias("alias_blik", "label", "application_key")
.build();
```

For details see Using default views.

Then, use the client allowing to send the transaction and handle the responses:
```
TpayClient.getInstance().postBlikTransaction(key, transaction, 
new TpayBlikCallback<TPayBlikResponse>() {
@Override
public void onResponseSuccess(TPayBlikResponse response) {
// Success means only, 
// that the transaction was sent correctly.
// The result depends on the response received.
}

@Override
public void onResponseError(ResponseBody errorBody) {
// An error occurred while sending the transaction.
}

@Override
public void onResponseFailure(Throwable t) {
// Throwable error occured when sending the transaction.
// E.g. no internet connection.
}
});
```

Details regarding API responses and error codes can be found in the API documentation on pages 6-13.

History of changes
------------------
Version 1.0 (June 2015) Version 2.0 (May 2017) Version 3.0 (July 2017)
