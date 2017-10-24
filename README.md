Para usar los respectivos keys desde la parte de gradle.properties debemos poder acceder a nuestros desde el archivo de gradle
por lo que procedemos a especificar las librerías que hemos usado

    retrofitVersion='2.3.0'
    dbflowVersion='4.1.1'
    butterknifeVersion='8.8.1'
    facebookVersion='[4,5)'
    eventBusVersion='3.0.0'
    daggerVersion = '2.0.1'
    glideVersion='4.0.0'
    jsr250Version = '1.0'

agragamos las respectivas dependencias necesarias para que las anotaciones sean accedidas y funcionene correctamente
luego, como tenemos una parte release y debug procedemos a usar nuestros apiKeys definidos desde el gradle.properties

resValue        Ayuda a crear un string con el nobre tal y tiene el valor tal , en este caso estoy concatenando las respectivas archivos
buildConfig     puedo acceder desde el código a esta variables, como una variable estática


CUando hacemos un deploy, debemos tener en cuenta que esto no debe ser HumanRedeable

Dentro de la documentación de Facebook, ya no es necesario que haya una clase Application que me ayude a inicializar mi aplicación por lo que usamos directamente los respectivos datos que nos proporcionaron

1ero creamos nuestro metadata que me ayuda a recibir mi facebook App ID
            <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/FACEBOOK_APP_ID"/>
2do creamos mi actividad que será la que interacturá con facebook, para un login puede ser
        <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

3ro creamos el provider que me ayudará a que pueda comentar compartir datos en facebook

        <provider android:authorities="com.facebook.app.FacebookContentProvider{APP_ID}"
          android:name="com.facebook.FacebookContentProvider"
          android:exported="true"/>


Commit1 ":CnfDb_Pojo"

Incluir base de datos dentro aplicación(DBFlow) pasos:

1.- Importar en Gradle(ya está)
2.- Vamos a incluirlo en mi "AplicationClass"

        Vamos a tener un "initDB" este método lo vamos a
        crear y en el método "initDB" escribimos "FlowManager.init(this)" pero no solo es necesario
        iniciarlos, sino también que lo termine, entonces en "onTerminate" además de llamar
        al ancestro vamos a hacer un "DBTearDown" con "FlowManager.destroy"

                    @Override
                    public void onCreate() {
                        super.onCreate();
                        initDB();
                    }
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        DBTearDown();
                    }
                    private void initDB() {
                        FlowManager.init(this);
                    }

                    private void DBTearDown() {
                        FlowManager.destroy();
                    }
3.- Registro Base de datos

    Registro paquete db y dentro de este paquete vamos a crear "recipesDatabase"
    esta clase tiene algunas características que nos van a servir para configurar la base
    de datos o usar por "dbFlow" vamos a definir aquí dos constantes:
            1.- La versión, podemos ir actualizando la version de acuerdo a como cambiemos nuestras tablas
            2.- Nombre de la base de datos

    Recordemos que "dbFlow" está trabajando por encima de "SQLite" enotnces usa anotaciones, pero al final sigue siendo "SQLite"

    Para usar la base de datos de nuestro DBFlow
    @Database        Esto usamos para que DBFlow reconozca nuestra base de datos, y le ponemos los atributos puestos dentro de "RecipesDatabase.class"

                @Database(name = RecipesDatabase.NAME,version = RecipesDatabase.VERSION)


    Con esto está configurada la base de datos, ahora puedo crear elementos que pueden usar entities (POJO) definidas como Clases


    Hacemos
        package "entities
            classPojo "recipe",la clase contiene lo necesario para mostrar una receta


            @Table(database = RecipesDatabase.class)
            public class Recipe extends BaseModel

Pilas necesitamos lo necesario para mostrarme un receta almacenada, entonces voy a tener,

            "RecipeID, Title,ImageURL,SourceUrl,Favorite"


            1.- Un identificador "private String recipeID"  este identificador va ser la llave primaria
                entonces le ponemos una notación para "PrimaryKey"
                Pero además de eso al irlo a traer al "API" no se va llamar "recipeID" así como lo puse aquí, con la D minúscula, sino tiene otro nombre, entonces
                le vamos a poner "SerializedName" indicando cual es este otro nombre, que va ser "recipe_Id"

                    @SerializedName("recipe_id")
                    @PrimaryKey
                    private String recipeId;

            2.- otro campo, el título de la receta y esto lo vamos a adornar con la notación "column" de la misma forma voy a tener un "ImageURL"
                para tener la dirección de la foto de la asociada con la receta, que también va ser una columna

                    @SerializedName("image_url")
                    @Column
                    private String imageURL;
            3.- Lo mismo con Source ImageURL

                    @SerializedName("source_url")
                    @Column
                    private String sourceURL;

                ....


Heredamos de de "baseModel" para que al compilar lo que
va ser "DBFlow" es generar varios métodos y algunas clases, sobre eso que le estamos,
dando
perdón



Recordemos que podemos agregar o quitar y comparar, entonces
cuando compare dos recetas, sobre todo esto lo voy a hacer con estructuras de datos por
ejemplo con "arrays" o para verificar si existe una receta entonces vamos a recibir un objeto
y este método es sobrecargado,
        entonces tenemos que ponerle "override" lo que vamos a verificar aquí,
        ahora definimos una variable, es si, este objeto que estoy recibiendo, es una receta,
        entonces, hacemos una validación, vamos a hacer un "typecasting" a convertir ese objeto
        a una receta listo, y asignamos la comparación a un "equals" de los identificadores, entonces
        comparo, el identificador propio de mi objeto, con el que estoy recibiendo y eventualmente
        pues eso es lo que voy a regresar y acaso si estoy comparando con un objeto que no es
        de tipo "recipe" de entrada, voy a regresar falso,

            @Override
            public boolean equals(Object obj) {
                boolean equal=false;
                if (obj instanceof Recipe){
                    Recipe recipe= (Recipe)obj;
                    equal=this.recipeId.equals(recipe.getRecipeId());
                }
                return equal;
            }


Con esto tengo listo mi modelo, que representa la receta guardad y tengo lista mi base
de datos para poder interactuar, noten que la parte de base de datos lo trabajamos en
base a anotaciones, eventualmente pues se van a generar unas clases, con las que voy
a poder interactuar, a través de esta misma clase "recipe" que va tener métodos adicionales,
por heredar de "baseModel"


4: API Retrofit

Comenzamos a incoporar Retrofit a nuestro proyecto para descargar los datos del servidor (Food2Fork), configurándolo de la siguiente manera:

-api
    --RecipeService*
    --RecipeClient*
    --RecipeSearchResponse

RecipeService,(interfaz) exponemos como vamos a realizar las peticiones, usamos
    @Get("search")

    Call<RecipeSearchResponse> search(@Query("key") String key,
                                      @Query("sort") String sort,
                                      @Query("count") int count,
                                      @Query("page") int page);

    De la api "search", me va a devolver un RecipeSearchResponse, recibiendo como parámetros, con esto exponemos nuetro API para poder hacer la llamada a nuestro servicio
        como Query api  key             apiKey
                        sort            tipo de ordenamiento especial por los mas recientes
                        count           va a ser uno
                        page            como un número aleatorio


RecipeClient(class)
    Vamos a definir un objeto "Retrofit" este objeto puedo hacer dos cosas
        -puedo construirlo en el cliente
        -puedo recibirlo como un parámetro

En el caso de recibirlo, lo que haría es inyectarlo.
al construirlo, lo estoy trabajando aquí adentro, entonces la diferencia básica va,
ser el tipo de prueba que voy a hacer sobre este cliente, porque si lo recibo como un
parámetro, entonces el testing va ser un poco más granular,
Par nuestro caso vamos a dejarlo  aquí encerrado, sabiendo que cuando haga las pruebas, voy a poder hacer
una prueba por encima, no voy a poder hacer la prueba interna, cambiando por ejemplo,
a donde hago la petición o cambiando como estoy reconociendo o haciendo el proceso "parsing"
de lo que responde la petición, entonces como va quedar aquí adentro, aquí mismo
voy a definir, cual es el URL base que voy a utilizar en este caso es "food2fork.com/api/"

            private Retrofit retrofit;
            private final static String BASE_URL = "http://food2fork.com/api/";


Creamos un constructor que no recibe nada, y en el constructor vamos a asignar
a la variable "Retrofit" un "Retrofit.Builder" y este "Builder" va tener unos parámetros
como "baseUrl" y usamos la que está aquí especificada, un "addConverterFactory(GsonConverterfactory)"
punto "create" sino quisiéramos usar "Gson" podemos usar "Jackson" o no usar ninguno, usar
el que trae por defecto "HTTP" lo que no me va permitir lo que en este caso el "API" está
enviando, por ultimo le damos construir,
        public RecipeClient() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


este cliente a la vez va tener un servicio,
entonces vamos a hacer un método "public RecipeService getRecipeService" no recibe
ningún parámetro y vamos a devolver a partir del cliente, la creación de este servicio,
indicando "RecipeService" que clase es la que tenemos

        public RecipeService getRecipeService() {
            return retrofit.create(RecipeService.class);
        }



RecipeSearchResponse
La respuesta incluye dos cosas, un conteo de cuantos "Recipe"
o cuantas recetas tenemos de respuestas y el listado de estas recetas, para nosotros
este conteo siempre debería ser uno, pero como le vamos a enviar un numero aleatorio,
es posible que algunas veces sea cero, de todos modos vamos a agregar aquí el conteo,
y vamos a agregar también un listado de recetas que es lo que vamos a recibir, con esto vamos
a agregar un "getter" y un "setter" para ambos, para que "Gson" ponga los valores adecuados

    private int count;
    private List<Recipe> recipes;

    //getters y setters

Pero además vamos a agregar un método que nos va ayudar a nosotros a obtener la primera
receta que está en este listado, entonces lo que vamos hacer primero, declaramos
una variable asignada a "null" vamos a devolver esta variable eventualmente pero antes de
devolverla verificamos: será que "Recipes" esta ¿vacío? en caso no está vacío, entonces
vamos a asignarle a "recipes.get(0)" estamos obteniendo la primera receta
Nos va servir para nuestro repositorio donde mandamos a llamar
-las recetas nuevas, llamar a este método(recipesGetFirst)
-a partir de la respuesta que va recibir el servicio
-el servicio lo voy a construir a partir del cliente

Entonces es una cascada de clases, que voy a estar utilizando, pero
con esto tengo listo lo que necesito con para "Retrofit" ya puedo hacer peticiones al "API"

Retrofit

http://square.github.io/retrofit/


5: SetupLibs
Comenzamos a realizar nuestra integración con librerías de acuerdo

-lib
    --base
        EventBus
        ImageLoader
    --di
        LibsModule
    GlideImageLoader
    GreenRobotEventBus

Nos ayudamos de las librerías del proyecto anterior como era "TwitterAPP"
Ahora en el "ImagesLoader base" voy a agregar
un método que se llama "setOnFinishedImageLoadingListener(Object object)" de tal forma que ejecute este "Listener"
cuando termine la carga como el "imagesLoader" esta genérico, estoy recibiendo un objeto
"Glide" tiene un tipo de objeto especifico,
pero si lo pongo aquí, estoy amarrando este "imageLoader" hacia "Glide" y no quiero hacer
eso
    public interface ImageLoader {
        void load(ImageView imgAvatar, String url);
        void setOnFinishedImageLoadingListener(Object listener);
    }



Clase "GladeImageLoader"
va ser muy parecida la que ya tenía, con algunas cosas adicionales, en el caso anterior
vamos a tener un "implements ImageLoader"y nos forza a tener
métodos, vamos a definir aquí de la misma forma del caso anterior:
    "RequestListener"   "glideRequestManager"
    "RequestManager"    "requestListener"

Constructor en el que vamos a recibir únicamente el "manager"

en setOnFinishedImageLoadingListener
hacer un "cast" el método es "set" lo único que tengo que hacer aquí, es verificar que
el objeto que estoy recibiendo, sea instancia de "RequestListener" y si, si lo es, entonces
lo asignamos, haciendo un "cast" asignamos aquí el "listener" es un objeto, vamos a
tener que hacer el "cast"

Ahora hasta el momento habíamos cargado aquí, sin asignar
este "listener" entonces vamos a verificar si acaso, el "listener" es diferente de "null"
vamos hacer una asignación ligeramente diferente pero si no, va ser muy parecido a lo que ya
teníamos que era "RequestManager" punto "load" cargamos el "URL" punto lo vamos a poner en
"Caches" ".diskCachesStrategy(DiskCacheStrategy.ALL)" ".centerCrop" ".into(ImageView)"
    //debemos tener en cuenta que esto está hecho con RequesOptions para RequestManager verificar nuestro analisis

        private RequestManager glideRequestManager;
        private RequestListener onFinishedLoadingListener;

        public GlideImageLoader(RequestManager glideRequestManager) {
            this.glideRequestManager = glideRequestManager;
        }
La diferencia
va ser que si tengo un "imageLoaderListener" previo a hacer el "into" le vamos a poner
".listener(onfinishedLoadingListener)", entonces en el caso de que previo a llamar
a "loading" haya hecho un "set onfinishedLoadingListener" en ese caso se va a mandar a llamar este "listener"
por si acaso quisiera en un momento no usarlo, tengo esta alternativa de cargarlo, sin necesidad
de hacer uso del "listener"

        RequestOptions requestOptions = new RequestOptions();
            requestOptions
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


        if (onFinishedLoadingListener!=null){
            glideRequestManager
                    .load(url)
                    .apply(requestOptions)
                    .listener(onFinishedLoadingListener)
                    .into(imgAvatar);
        }else {
            glideRequestManager
                    .load(url)
                    .apply(requestOptions)
                    .into(imgAvatar);
        }

di
    LibsModule

Inyección de dependencias, voy a usar de referencia del módulo de la aplicación anterior,
entonces creamos aquí, el "libsModule" vamos a corregir el "import" porque era de "TwitterClient"
y ya no tiene nada que ver con lo que tengo actualmente, tiene el "imageLoader" "EventBus"
necesita que lo importe, entonces vamos a importar la base adecuada que es "libs.base"
pero ahora ya no voy a estar trabajando con un fragmento si no con una actividad, porque
en el caso anterior, tenía un fragmento como encargado de devolver la App de realizar la
carga de datos y ahora va ser una actividad, entonces corregimos esto y corregimos el método
que provee, Estamos proveyendo el "imageloader"
el "RequestManager" la actividad, todo está listo, ahora puedo proceder a hacer uso de
estas librerías.


    private Activity activity;

    public LibsModule(Activity activity) {
        this.activity = activity;
    }


