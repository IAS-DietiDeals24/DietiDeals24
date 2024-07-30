# Panoramica REST

## Lato client

Innanzitutto rendiamo true il seguente campo nel manifest dell'applicazione Android per consentire
di testare in locale lo scambio di messaggi:

```XML
android:usesCleartextTraffic="true"
```

La comunicazione non sicura è inibita di default (poiché i messaggi sono scambiati in chiaro) e
tenerlo a false impedirebbe il test in locale.
Ovviamente al momento della produzione, dovrebbe essere disattivato per evitare problemi di
sicurezza.

### RestObjects.kt

Il codice nel file RESTObjects.kt definisce due oggetti Kotlin (quindi due classi già instanziate, a
mo' di Singleton, ma questi oggetti sono istanziati "staticamente" e quindi possono essere
richiamati ovunque).
I due oggetti in particolare sono:

1. RetrofitController;
2. APIController.

#### Importazioni

```Kotlin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
```

L'artefatto **com.squareup.retrofit2:retrofit** contiene la classe Retrofit, la quale è un ausilio
all'utente per la gestione alle chiamate API.
L'artefatto **com.squareup.retrofit2:converter-gson** contiene la classe GsonConverterFactory che
consente una rapida traduzione dei dati JSON ricevuti.

#### RetrofitController

```Kotlin
object RetrofitController {
    private const val BASE_URL = "http://192.168.1.217:55511"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

Il RetrofitController conterrà l'istanza della classe Retrofit.
Viene definito un URL, ossia **BASE_URL** che indicherà al controller l'URL del nostro programma
server in ascolto per le richieste API.
Per le prove, ho installato l'applicazione sul mio telefono e ho inviato delle richieste al mio PC (
il cui indirizzo sulla mia rete è quello sopracitato) alla porta **55511** che, nella configurazione
dell'immagine Docker del nostro programma server è la porta di ascolto sul client; questa porta sul
server è mappata alla porta 8080 (ossia quella utilizzata per il protocollo HTTP).
Ovviamente in occasione del deployment dell'applicazione l'indirizzo andrebbe modificato per puntare
all'indirizzo pubblico del programma (o relativo nome di dominio che sarà eventualmente risolto dal
DNS).
Il nostro oggetto ha un campo **instance**, che è effettivamente l'istanza dell'oggetto Retrofit.
Il campo viene inizializzato come **lazy**, ossia secondo i dati forniti dall'utente e solo quando
viene utilizzato per la prima volta, non quando viene creato RetrofitController (molto presto
essendo un oggetto statico) per non cominciare ad allocare memoria prematuramente.
Quindi si specifica l'URL di base, la volontà di utilizzare GSON ed infine la costruzione
dell'oggetto.

#### APIController

```Kotlin
object APIController {
    val instance: API by lazy {
        RetrofitController.instance.create(API::class.java)
    }
}
```

Il controller API conterrà un riferimento all'interfaccia **API**, utilizzata per raccogliere le
funzioni che verranno utilizzate per effettuare richieste REST.

### API.kt

Il file contiene tutte le richieste REST da effettuare al server con tutte le informazioni
necessarie a formattare la richiesta nella maniera corretta.
Esso è un'interfaccia perché ovviamente il client ha semplicemente bisogno della risorsa ma non sa
come recuperarla o effettuare un'operazione su di essa, sa solo dove chiedere per quella risorsa e
cosa vuole fare.
Lato server, ci sarà un metodo appositamente costruito per gestire quella determinata richiesta
inviata ad un determinato endpoint, ossia il "luogo" dove viene gestita la richiesta.

```Kotlin
@GET("account")
fun accedi(@Query("email") accountEmail: String, @Query("password") accountPassword: String): Call<User>
```

L'annotazione **@GET** indica che questo metodo creerà una richiesta GET per il server.
Come argomento dell'annotazione è spcificato l'endpoint, **account**, che sommandosi all'indirizzo
fornito in **BASE_URL** diventerà (nel mio caso) **http://192.168.1.217:55511/account**.
Quando una richiesta è una GET, sappiamo che si possono specificare determinati parametri da inviare
al server per aiutarlo nella gestione della richiesta; questi costituiscono la query string.
La nostra query string ha una email e una password, e definiamo i nomi dei parametri mentre lasciamo
incognito il loro valore indicando che tali porzioni della query string vadano compilati a runtime.
Nella lista dei parametri formali del metodo, con l'annotazione **@Query** indichiamo che quel
determinato parametro sarà aggiunto all'URL della richiesta con il nome indicato come argomento
dell'annotazione e come valore quello del parametro attuale passato al metodo.
Infine, indichiamo i dati passati nel corpo della risposta del server, che solitamente dovrebbe
essere in formato JSON, in quale tipo di classe andrebbero deserializzati.
Possiamo fare ciò inserendo il nome della classe come parametro di tipo dell'interfaccia parametrica
**Call**.
Questa interfaccia è un wrapper per un oggetto Retrofit che può eseguire una chiamata API ad un
server.
Immaginiamo che la nostra classe User sia fatta così sul nostro client Kotlin (solo per fini
esplicativi):

```Kotlin
public class User(numero: Int, stringa: String)
{
    public var numero: Int = 0
    public var stringa: String = ""

    init {
        this.numero = numero
        this.stringa = stringa
    }
}
```

E così sul nostro server Java (si noti come le due classi corrispondano esattamente):

```Java
public class User
{
    public int numero = 0;
    public String stringa = "";
    
    public User(int numero, String stringa)
    {
        this.numero = numero;
        this.stringa = stringa;
    }
}
```
### ControllerAccesso.kt

Vediamo ora un esempio di come effettuare concretamente la chiamata API al server.

```Kotlin
val call = APIController.instance.accedi(viewModel.email, viewModel.password)

call.enqueue(object : Callback<User> {
    override fun onResponse(call: Call<User>, response: Response<User>) {
        if (response.code() == 200) {
            val user : User? = response.body()
        } else {

        }
    }

    override fun onFailure(call: Call<User>, t: Throwable) {

    }
})
```

Memorizziamo all'interno della variabile **call** l'oggetto che si occuperà di chiamare il metodo
sul server.
Passiamo ovviamente al metodo della nostra interfaccia API i dati che gli servono per completare la
richiesta correttamente (in questo caso password ed email inserite dall'utente all'interno della
UI).
Chiamiamo il metodo **enqueue** sul nostro oggetto per mandare la richiesta, poi passiamogli un
oggetto anonimo che implementa l'interfaccia **Callback** (specificando come parametro di tipo
sempre l'oggetto nel quale andranno deserializzati i dati della risposta).
Questa interfaccia denota un oggetto che può gestire le risposte del server dopo una chiamata API.
Deve infatti obbligatoriamente implementare due metodi:

1. onResponse;
2. onFailure;

#### onResponse

onResponse è il metodo che si occupa di gestire la richiesta quando questa è stata recepita senza
problemi (non tiene conto quindi del codice di stato della richiesta ma se la richiesta è stata
effettviamente recapitata).
I suoi parametri sono **call**, che è lo stesso oggetto creato prima del metodo enqueue e dunque
consente di interagirvi portandolo in scope all'interno dell'oggetto anonimo, e **response** che
immagazzina i dati della risposta.
Possiamo quindi ad esempio eseguire azioni diverse in base a se la richiesta è stata correttamente
gestita (codice di stato 200 della riposta, ottenibile attraverso la chiamata del metodo **code** di
response) oppure se non è stata correttamente gestita (codici 400 o 500) o eventuali altri casi (
codici 100 e 300).
In questo metodo possiamo ad esempio assegnare il body ad una variabile (come si può vedere in
**val user : User? = response.body()**) che automaticamente deserializza la risposta JSON in un
oggetto della classe User come è stato indicato.
Il punto interrogativo che segue User sta ad indicare che il valore della variabile può essere nullo,
poiché effettivamente non è detto che la risposta sia di successo e contenga i valori di User (non a
caso il tipo di ritorno di response.body() in questo caso è proprio User?, e il type checking
impedisce di assgenare User? ad User, ossia un valore possibilmente nullo ad uno asserito come non
nullo).

#### onFailure

onFailure è il metodo che si occupa di gestire la situazione nella quale la richiesta non è stata
recepita perché si è verificato un errore imprevisto (ad esempio un errore client come l'inserimento
di un indirizzo in BASE_URL errato).
Quando ciò si verifica, l'eccezione generata da tale errore è memorizzata nel parametro **t**, e
può quindi essere esaminato o rilanciato per gestirlo al meglio.
Per quanto riguarda **call**, svolge lo stesso ruolo che ha in onResponse.

## Lato server

Analizziamo ora ciò che succede sul server.
La nostra applicazione server deve essere in ascolto per poter recepire le richieste HTTP, quindi
avviamola attraverso l'IDE.
Ora, nella classe principale di Spring, analizziamo i vari elementi aggiunti al template di base di
Spring.

### Importazioni

```Java
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
```

Le importazioni riguardano delle classi e delle annotazioni che sono state inserite in frammenti di
codice successivi.

### Annotazione

```Java
@RestController
```

L'annotazione indica che questa classe è una classe che si occupa di gestire richieste di API REST
(ovviamente non è per semplice pulizia organizzativa, ma imposta dei parametri particolari per
l'applicazione).

### Metodo

Sempre supponendo che User sia definito all'interno dell'applicazione come indicato in precedenza e
importato in qualche maniera, alla nostra classe principale sarà aggiunto un metodo:

```Java
@GetMapping(value = "account", produces = MediaType.APPLICATION_JSON_VALUE)
public User accedi(@RequestParam("email") String accountEmail, @RequestParam("password") String accountPassword)
{
    return new User(22, accountEmail + accountPassword);
}
```

L'annotazione **@GetMapping** indica che la richiesta che deve essere gestita è una GET.
Esistono altri mapping alternativi per altri tipi di richieste, come **@PostMapping**,
**@DeleteMapping**, eccetera.
I parametri di questa classe di annotazione che per il momento ci interessano sono, tra i vari:

1. **value**, che indica l'endpoint al quale effettuare la chiamata;
2. **produces**, che indica il tipo di risposta che deve essere restituita.

Poiché Spring serializza automaticamente ciò che viene restituito nel formato specifcato, è
importante assicurarsi che venga specificato il valore corretto in questo campo per evitare problemi
da parte dell'interprete del client (che nel nostro caso si aspetta una risposta con un corpo JSON).
Ciò che viene specificato per produces è una classe enumerativa che fornisce vari possibili tipi di
risposta (in sostanza, modifica il campo Content-Type della richiesta HTTP).
Volendo possiamo restituire XML (in stile SOAP), PDF, testo piano, o altro.
Il metodo ha un nome da noi scelto (per comodità e coerenza lo chiameremo allo stesso modo di come è
stato chiamato sul client), visibilità pubblica e come tipo di ritorno ciò che deve essere
serializzato al momento della costruzione della risposta, in questo caso la classe User con i suoi
campi.
Nei parametri formali del metodo, grazie all'annotazione **@RequestParam** possiamo accedere ed
assegnare agli stessi i valori dei parametri passati con la query string (quale valore andrà preso
è determinato dal nome del parametro nella query string passata come argomento dell'annotazione).
Per prova, restituiremo un nuovo oggetto User inizializzato con i valori per i campi specificati
nel codice sopracitato.
Seguendo la struttura del metodo costruito sul client, immaginiamo di mandare una richiesta, il cui
URL sarà:

```
http://192.168.1.217:55511/account?email=mario.rossi@gmail.com&password=MarioRossi
```

Allora la risposta del nostro server sarà:

```JSON
{
    "numero": 22,
    "stringa": "mario.rossi@gmail.comMarioRossi"
}
```

Ossia un oggetto che ha token la cui chiave è il nome dei campi della nostra classe e come valori i
valori di tali campi.
Quando questo corpo sarà passato al client, Retrofit si occuperà di deserializzare tali valori in un
oggetto, nel nostro caso User.

## Un esempio con una POST

Vediamo ora velocemente come fare una richiesta POST; solitamente in una POST dei dati sono passati
dal client al server attraverso il body della richiesta, mentre la risposta sole essere vuota (ma
per motivi di debug o altri si può sempre popolare con dei dati anche quest'ultima).

Nel nostro client, possiamo aggiungere all'interfaccia API il metodo:

```Kotlin
@POST("account/uploadPhoto")
fun caricaFoto(@Body photo: ByteArray): Call<Unit>
```

Che definisce una POST, il cui contenuto del corpo è rappresentato dal valore del parametro
**photo** del metodo, il quale è denotato dall'annotazione **@Body**.
La nostra foto potrebbe essere rappresentata da un array di byte (ovviamente è solo a titolo di
esempio).
Vogliamo inoltre che la nostra risposta non abbia un corpo; essendoci quindi nessuna necessità di
inserire dati deserializzati in un oggetto possiamo passare a Call il tipo **Unit**, che per Kotlin
è l'analogo di void per Java.

Sul nostro server, aggiungiamo il seguente metodo:

```Java
@PostMapping(value = "account/uploadPhoto")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void uploadPhoto(@RequestBody ByteArrayResource photo)
{
    //logica del metodo
}
```

Come affermato in precedenza, per una POST possiamo utilizzare l'annotazione **@PostMapping** e
passare per argomento all'annotazione l'endpoint.
Vediamo inoltre che grazie all'annotazione **@ResponseStatus**, che accetta come argomento un
valore della classe enumerativa **HttpStatus**, possiamo specificare quale codice di stato verrà
comunicato al client nella risposta.
In questo caso, vogliamo che il messaggio sia di successo (famiglia di codici 200) ma sia chiaro che
il body è vuoto; possiamo quindi usare **HttpStatus.NO_CONTENT** per comunicare il codice di stato
204 nato per questo scopo.
Se avessimo lasciato il body della risposta vuoto e non avessimo cambiato il codice di stato, molto
probabilmente al client sarebbe stata restituita una risposta con codice di stato 405 (Method not
allowed).
Se non l'avessimo lasciato vuoto, invece, non avremmo avuto differenze con il caso della GET visto
in precedenza.
Bisogna ovviamente assicurarsi che il body e il codice che stiamo comunicando non si contraddicano,
quindi se comunichiamo codice 204 DEVE essere vuoto.
Di default, quindi quando non presente @ResponseStatus, il codice di successo è 200.
Infine, prendiamo il contenuto del body e lo inseriamo in una variabile denotandola con
**@RequestBody**, e il suo tipo sarà **ByteArrayResource**, una classe Spring che funge da wrapper
per il nostro tipo ByteArray.

Sul client possiamo ora chiamare la API in maniera analoga alla precedente:

```Kotlin
val call = APIController.instance.caricaFoto(ByteArray(256))

call.enqueue(object : Callback<Unit> {
    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
        if (response.code() == 204) {

        } else {

        }
    }

    override fun onFailure(call: Call<Unit>, t: Throwable) {

    }
})
```

Con il piccolo accorgimento di cambiare il parametro di tipo di Call e Callback, il nome della
funzione chiamata su instance, e il controllo della risposta corretta.