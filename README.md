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



6: LoginLayout
Necesitamos generar nuestro boton de login de Facebook, debemos estar pilas si nos da error en DBFlow, ya que este está acostumbrado a
get y set, pero en recipe tenemos isFavourite, y nos puede dar un error, por lo que cambiamos a getFavorite o setFavorite

activity_login
    android:id="@+id/container"
Usamos este contenedor para mostrar snackbarcks de acuerdo al conteneiner definidos

    <com.facebook.login.widget.LoginButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/btnLogin"
        android:layout_centerInParent="true"/>
Usamo el login Button para ver como es la funcionalidad de nuestro botón

Solucinar Error de Compilación para FacebookActivity
https://stackoverflow.com/questions/39754070/how-to-solve-facebook-toolsreplace-androidtheme

7: LoginConfigFacebook

Procedemos a realizar la configuración del login de facebook
-login
   --ui
   ----LoginActivity

En LoginActivity
    Realizamos las inyecciones de las vistas con Butterknife en este caso solo:
        @BindView(R.id.btnLogin)
        LoginButton btnLogin;
        @BindView(R.id.container)
        RelativeLayout container;

    Necesitmaso un callback manager de facebook,  y la declaramos como una varible globla, en la documentación de facebook este campo se usa para
    recordar si alguien accedió a la aplicación

        private CallbackManager callbackManager;
    Luego del Binding(Butterknife)
        hacemos la llamada al botón y creamos nuestro callback manager
            callbackManager= CallbackManager.Factory.create();
            //Podemos decrile que tenga ciertos permisos, y podemos pedirlos mas tarde con el SDK si queremos
            btnLogin.setPublishPermissions(Arrays.asList("publish_actions"));
            //la siguiente es que cuando lo accedemos entrar a Success, Cancel, onError
            para cada uno de estos la lógica como ya hemos definido
                    btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            navigateToMainScreen();
                        }

                        @Override
                        public void onCancel() {
                            Snackbar.make(container, R.string.login_cancel_error, Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            String msgError = String.format(getString(R.string.login_error), error.getLocalizedMessage());
                            Snackbar.make(container, msgError, Snackbar.LENGTH_SHORT).show();
                        }
                    });
            Luego vamos al método
                navigateToMainScreen()
                //agragamos banderas para que no haya historia
                    Intent intent=new Intent(this,RecipeMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

            Es posible qu tengamos una sesión iniciada es importante que valide si tengo una con AccessToken

                    if (AccessToken.getCurrentAccessToken() != null) {
                        navigateToMainScreen();
                    }

            En onActivityResult podemos llamar a callbackManager con los mismos parámetros que tenemos dentro de este, con esto va a administrar la sesión
                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
        RecipeMainActivity
            Solo la definimos para ver si ya funciona


En string.xml
        login.error Imposible iniciar sesión %s   -----: este porcentaje S qeu ocurre y que le dé un formato


Nota: Problemas dados
1.- Porque no puedo hacer login con una cuenta diferente a la desarrollador
2.- Al momento de usarlo debemos tener en cuenta los paquetes y la clase en donde se realiza el login



Inicio de Login
https://developers.facebook.com/docs/facebook-login/android


7: ImplementsMainRecipes_Layout

Nos toca ahora implementar la pantalla principal de la recetas, que vamos a tener en la pantalla
principal
    1: Primero en la barra superior
            tenemos un ícono va ser un elemento de menu pero con icono asignado, que nos va a llevar otra pantalla, a la pantalla de favoritos guardados
            vamos a tener aquí la posibilidad de cerrar sesión, con el menu de los tres puntos, el contenido principal de "ImageView" que nos va mostrar la imagen de la receta,


    2: Luego dos botones,
            uno para rechazar y uno para guardar
            Además vamos a poder hacer "swipe" es decir el efecto de arrastrar, hacia la derecha para conservarlo y hacia la izquierda para rechazarlo

vamos a empezar implementando este "Layout" para el manejo del "swipe"

    3: Vamos a agregar un "ProgressBar" que aquí no se ve, en lo que cargamos en el siguiente contenido



Podríamos haber usado un "ViewPage" talvez, es un componente que está pensado
que pueda tener varios contenidos, fragmentos usualmente, me refiero al "ViewPage" y aquí
yo quiero este mismo contenido, se quede siempre y solo cambie la imagen, a la hora de hacer
un "drag" hacia cualquiera de los dos lados, Por lo que hemos decidido hacer un detector de gestos
para manejar este "swipe" vamos a ponerle una animación, que nos muestre cuando va
saliendo hacia un lado y hacia el otro




Tenemos un "RelativeLayout"
que ya existe en este momento, vamos a agregar algunas cosas, como un "ImageView" y vamos
a agregar también un "LinearLayout" horizontal, para los botones, centrado y dentro de ese
"LinearLayout" vamos a agregar "ImageButton" otro "ImageButton", por ultimo agregamos
un "ProgressBar" que este centrado


En Source
un identificador al contenedor principal, este identificador, va ser "LayoutContainer"
y luego tenemos el "ImageView" quiero que este centrado horizontalmente, quiero que
este alineado, con el "parent" y quisiera que tenga cierto margen, porque
como esta como con "wrapContent" el tamaño me puede salir diferente, primero, voy a querer
ponerle un margen a la izquierda "marginLeft" vamos ceder el "Activity_Horizontal_Margen"
y lo mismo hacia la derecha,


Tenemo un "LinearLayout" horizontal, esto
queremos que lo ancho, utilice únicamente lo necesario, va ser "wrap_content" de alto
también, va estar centrado de forma horizontal y le vamos a poner un identificador, hasta
"below" el "ImageView" tiene identificador como "imgRecipe" entonces va estar "below" de "recipe" y también
va tener su propio identificador, este "Layout", le vamos a poner "LinearLayout"



Para los buttons "ImagesButtons" el primero
va tener de "Id/ImgDismiss" y el segundo "imgKeep"



Usamos los íconos
    icono "delete", para cuando no quiero conservarlo,
    ícono que se llama "btn_star_big_on" estos son los dos iconos que tengo


    Para cambiar el color puedo usar tint o background




Luego el "progressBar" es de tamaño grande, "wrap_content",
tiene un identificador, vamos a ponerle que este centrado, en el padre, "centerInParent = true" entonces
le vamos a agregar que la visibilidad este "Gone" a ver cómo nos queda, "View" "Visivility Gone" de la misma forma vamos a quitarle el "source" que tenía el "ImageView" antes de
quitárselo voy a hacer algo al "height" le voy a poner un tamaño fijo, entonces vamos
a referenciar a "dimen" y "dimen/recipes_main_images_height"
le vamos a poner un valor fijo, "300DP", solo me aseguro que todo esté bien, este es el
tamaño que va tener, la imagen, se ven los botones, sin ningún problema, y ahora sí
le puedo quitar el "source" para continuar con la implementación.



Commit8: MVP_RecipeMain

Creamos la estructura MVP

-recipemain
    --events
        RecipeMainEvent
    --ui
        view
Como ya sabemos comenzamos por la vista
En recipeMainView interfaz

Aquí vamos a colocar todo lo necesario para la vista, vamos a tener un "progressBar" entonces necesito
mostrar ese progreso o esa barra de progreso y también necesito ocultarla, a la vez, voy
a tener elementos de "ui" que voy a querer mostrar y ocultar "UIElements", en base al
"ProgressBar" es decir el "Progressbar" no va estar al mismo tiempo que los elementos
de "ui" vamos a tener también animaciones que el presentador se va encargar de llamar,
entonces.
Vamos a tener una animación para salvar la receta y otra para no salvarla,
Luego, cuando decida salvar, la receta, vamos a tener de cierta forma una reacción de la
vista, y a la vez, es posible que esta receta no sea la única que yo visualice, sino que
visualice varias, entonces voy a tener un "setRecipe" cuando lo reciba, para a cambiar
la imagen y voy a recibir los datos de la receta, esos datos son los que voy a guardar
si decido hacerlo, y vamos a poner un "onGetRecipeError" por si acaso, por si tenemos un error, ok,
que pasa, si decido, deshacerme de esta receta, en este caso yo no voy hacer nada, pero una
posible acción seria, guardar el identificador en un listado, persistente en la base datos,
y cuando saque el numero aleatorio asegurarme que ese listado no está allí, aquí voy
a correr el riesgo que el numero aleatorio se repita vamos a hacer un rango amplio,
pero es posible que se repita y en ese caso pues, el usuario va ver dos veces, la misma
receta es posible que ya este guardada, o es posible que haya decidido no conservarla,

    void showProgress();
    void hideProgress();
    void showUIElements();
    void hideUIElements();
    void saveAnimation();
    void dismissAnimation();

    void onRecipeSaved();
    void setRecipe(Recipe recipe);
    void onGetRecipeError(String error);


Vamos a crear una nueva interfaz para el presentador le llamamos "RecipeMainPresenter"
y es una interfaz, aprovechando esto vamos a crear de una vez el paquete de "ui" y vamos
a mover allí, tanto la actividad, como su respectiva vista, van a incluir a "onCreate", "onDestroy" para
registrar, deregistrar y destruir la vista, y luego las acciones posibles que tengo, ósea
que tengo un "dismissRecipe" con lo que no voy a guardar nada en ningún lugar, si me
interesa llamar la animación de la vista un "getNextRecipe" para obtener el siguiente
receta de cocina, un "SaveRecipe" en donde me interesa que este objeto se quede guardado
en la base de datos, y el evento en el que voy a recibir, información del "eventBus"
entonces "recipeMainEvent" va ser lo que reciba aquí,
Creamos el Event de una vez
Voy a agregar un método adicional, que este me va a servir con motivos de "Testing" vamos a hacer un
"RecipeMainView getView" y varias de las decisiones que voy a tomar en la implementación de esta
aplicación van a ser por hacer "Testing" y eventualmente vamos a hacer pruebas de esta
aplicación en la última semana, se los comento porque este "getView" no lo voy a mandar a
llamar aquí. pero me va servir para hacer la prueba, de que la vista se volvió "null"
al llamar un "destroy",

    void onCreate();
    void onDestroy();

    void dismissRecipe();
    void getNextRecipe();
    void saveRecipe(Recipe recipe);
    void onEventMainThread(RecipeMainEvent event);

    RecipeMainView getView();



Una interfaz, para el interactuador, voy a tener dos interactuadores, vamos a llamarle
a uno "SaveRecipeInteractor" y a este interfaz le vamos a poner un único método "execute"
que recibe un objeto a guardar

public interface GetNextRecipeInteractor {
    void execute();
}

Vamos a hacer también un, "getNextRecipeInteractor" y a este le vamos a poner un método "execute" que no recibe nada,

public interface SaveRecipeInteractor {
    void execute(Recipe recipe);
}


Para terminar
estructura vamos a hacer un "RecipeMainRepository" aquí vamos a poner los métodos necesarios
"getNextRecipe" "saveRecipe" y también "setRecipePage" la idea es que voy a generar un numero aleatorio en el interactuador
o en algún otro lugar solo el repositorio va exponerlo y con ese número aleatorio voy
a hacerle un "set" para saber que numero voy a pedir, además voy a tener un par de constantes
que vamos a colocar aquí en el repositorio, estas constantes están relacionadas con el
"API" y son:
    -cuantas recetas quiero obtener, voy a obtener una nada más,
    -de qué forma voy a ordenar
    -el rango de recetas que tengo, este rango es un poquito empírico estoy asumiendo
    que son "100,000" hice un par de pruebas y si aumenta, tengo muchas ocurrencias que no
    recibo nada, si disminuye sigue funcionando, pero se me hizo que era un buen número de
    nuevo, llegue a, el de forma ensayo y error entonces, no necesariamente es el mejor,
    pero es con el que vamos a trabajar,


Con esto tenemos lista la estructura, todas las interfaces
y puedo pasar a la implementación



Un comentario, es posible que tantos archivos.
sean molestos para algunas personas, en mi caso, yo prefiero trabajar así para saber
a qué archivo dirigirme, sin embargo, como son interfaces, podrían colocarse todas dentro
de un solo archivo que se le llame por ejemplo "RecipeMainContract" porque es el contrato
que va a implementar y dentro implementamos todas estas interfaces, estas por lo menos
una para la vista, una para el presentador, talvez hay un interactuador, talvez hay un
repositorio, aunque pueden haber caso que no lo haya, entonces podríamos implementarlo
así, mi forma de trabajar va continuar de esta manera, pero cada uno de ustedes lo puede
implementar de la forma que se sienta más cómodo.


Commit9: RecipeMainActivity_functions

En actividad RecipeMainActivity

    -Inyección de Butterknife
        -ImageView
        -imageButtons
        -progressBar
        -etc

    -Definimos un presentador que se inyectará y lo usaremos para nuestra lógica

    -Editamso onCreate
        -setupInjection//que definiremos como se inyecatará luego, y con esto nos aseguraremos que el presentador fue inicializado
        -getNextRecipe()//para traernos una receta
        -onDestroy
            presenter.onDestroy


    -Implementamos RecipeMainView
        -ShowUIElements//mostramos botones
            imgKeep.setVisibility(View.VISIBLE);
            imgDismiss.setVisibility(View.VISIBLE);
        -HideUIElements
            imgKeep.setVisibility(View.GONE);
            imgDismiss.setVisibility(View.GONE);
        -ShowProgress
            progressBar.setVisibility(View.VISIBLE);
        -HideProgress
            progressBar.setVisibility(View.GONE);

        -saveAnimation
        -dismissAnimation


    -//necesitamos manejar los clicks por lo que agregamos
        "public void onKeep" y este método me va a servir cuando tenga un "onClick"
        llamamos al presentador y hacemos que se valide que existe algo que guardar y para eso necesito un objeto, con
        lo que voy a conservar aquí que quiero guardar, entonces, le vamos a llamar "CurrentRecipe" definido en las variables locales

            private Recipe currentRecipe;

        Voy a verificar que si ese elemento es diferente de "null" entonces que si lo
        guarde "Presentar.saveRecipe(currentRecipe)"

            @OnClick(R.id.imgKeep)
            public void onKeep() {
                if (currentRecipe != null) {
                    presenter.saveRecipe(currentRecipe);
                }
            }


        Luego voy a hacer uno similar para "dismiss"

            @OnClick(R.id.imgDismiss)
            public void onDismiss() {
                presenter.dismissRecipe();
            }


        -"string.xml" ahora aquí dos mensajes, para cuando él la receta fue guardada exitosamente
        y cuando fue imposible obtener la información del "API" regreso a mi "recipeMainActivity"
        aquí vamos hacer un "SnackBar.make(LayoutContainer. R.String.recipemain_notice_saved, SnackBar.LENGTH_SHORT).show" y para "RecipeError"
        voy a hacer algo similar, solo que aquí si tengo un formato, si recordamos el "String.xml"
        tiene un porcentaje "S recipeError" entonces vamos hacer un "string msgError = String.format(getString(R.string.recipemain_error), error)" y esto es lo que le envío al "snackBar" cuando hay
        un error,

            <string name="recipemain.error">Imposible obtener información %s</string>
            <string name="recipemain.notice.saved">Guardado exitosamente</string>

        por ultimo "setRecipe" entonces lo que voy a hacer es modificar el valor de
        "CurrentRecipe" este método lo llama el presentador, entonces aquí lo estoy colocando y voy a
        necesitar cargar la imagen, para eso necesito un "imageLoader" entonces vamos a definir
        aquí también un "private imageLoader ImageLoader" listo, configuramos un poco aquí, y sobre
        este "ImageLoader" voy a realizar la carga al tener el "recipe" entonces le hacemos "load"
        el "ImageView" se llama "recipe" y lo que le envío es "recipe.getImageURL" antes de
        hacer esta llamada debería haber configurado un "louder" tenga un "listener" para cuando
        termino y en ese momento muestre todo lo necesario, todos los elementos de "ui" entonces por el
        momento vamos a dejarlo aquí y tengo lista la parte inicial de mi actividad y la vista.

                @Override
                public void setRecipe(Recipe recipe) {
                    this.currentRecipe = recipe;
                    imageLoader.load(imgRecipe, recipe.getImageURL());
                }


Commit10  :Main_Activity_Menu_Image_Loader


Trabajamos
    -menú para cambiar de pantalla
    -cerrar sesión


Creamos
    -menu
        "MenuResourceFile" le llamamos "menu_recipes_main.xml"

    -strings
        Una para indicar cuál es el nombre de la acción que me va a llevar a la pantalla de recetas guardadas, y empieza
        con "recipeMain" porque pertenece a esta pantalla

        Otra es global porque va estar disponible en las dos pantallas  "global.menu_recipes_main"

Para el menú
Uno de ellos tiene un icono que es la estrella, la estrella me va permitir mostrarlo, como
una acción, entonces le indico que siempre tiene que ir como una acción es decir fuera de los 3 puntitos "app:showAsAction="always""
Y el "Logout" no es una acción nunca, por lo tanto va estar valor el menu, bajo los tres puntitos, eso es lo que marca
la diferencia de cómo se coloca este menu, ahora vamos a encerrar estos archivos "app:showAsAction="never""



        <menu xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            tools:context=".recipemain.ui.RecipeMainActivity">
            <item
                android:id="@+id/action_list"
                android:title="@string/recipemain.menu.action.list"
                android:icon="@android:drawable/btn_star_big_off"
                app:showAsAction="always" />

            <item
                android:id="@+id/action_logout"
                android:title="@string/global.menu.action.logout"
                app:showAsAction="never" />
        </menu>

En recipeMainActivity

        Sobrecargar los métodos de menu
        "onCreateOptionsMenu"
            Inflamos los respectivos menús
        "onOptionsItemMenu"
            Aquí inflamos el menu y aquí vamos a validar,
            tenemos dos posibles opciones, entonces vamos a decir "id=item.getItemId"
            entonces si este identificador es "ActionList" voy a moverme hacia otra actividad
            entonces aquí vamos a agregar un método "navigateToListScreen"
            si es "ActionLogout" voy a cerrar sesión, el método "logout"

             if (id == R.id.action_list) {
                        navigateToListScreen();
                        return true;
                    } else if (id == R.id.action_logout) {
                        logout();
                        return true;
            }


           En "navegatorToListScreen" que va ser,no necesitamso que ser borren las banderas por lo que usamos directamente la actividad
           entonces hacemos un "new Intent(this, recipeListActivity.class)"


           Creamos un RecipeListActivity


           Creo el método "logout",como este método está en "RecipeMain" y en "RecipeList" vamos a usar el "ApplicationClass" tengo un "FacebookRecipes app" igual a voy
            a necesitar un "type Casting" aquí y escribo "getApplication" y sobre la aplicación voy
            a utilizar el método "logout" sin ningún parámetro,

            En RecipeMainActivity
                private void logout() {
                    FacebookRecipesApp app=(FacebookRecipesApp)getApplication();
                    app.logout();
                }
            En FacebookRecipesApp

                public void logout(){
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            Cerramos sesión con un "LoginManager" ".getInstance.logout" y la siguiente va ser crear el "intent" un
            "intent" nuevo a partir de "LoginActivity.class"y ponemos las banderas necesarias para limpiar la
            historia


Configuración del ImageLoader
RecipeMainActivity


Vamos a crear un método aquí, después de "SetupInjection" llamado "setupImageLoader"
la idea aquí es que vamos a tomar el "imageLoader" y le vamos hacer un "setOnFinishedImageLoadingListener"
y aquí instanciamos el objeto, de hecho vamos a hacerlo antes, para que no quede de esta
forma "RequestListener GlideRequestListener = new RequestListener" implementamos los métodos
y le vamos a enviar este valor al "imageLoader", recordemos por el momento el "imageLoader"
no existe entonces esto nos podría dar "null" por lo que comentamos esta mientras lo
inyectamos
 tengo dos métodos,
        "onException"
            Me va servir para mostrar un mensaje de error
        "OnResourceReady"
            Me va servir para ocultar la pantalla de progreso y mostrar los elementos de "ui" tengo dos formas de hacerlas


Podría directamente aquí, colocar en "onException" un "SnackBar.make" etcétera
y "onResourceReady" llamar a "hideProgress" y "ShowUIElements"

O cederle esto al presentador, es de nuevo, una decisión que puede tomar cada uno, en mi caso lo que voy a hacer, es
dirigirme al presentador y modificarlo, entonces vamos a abrir el "RecipeMainPresenter" y vamos
a agregar aquí un "void imageError(String error)" que va recibir el error, y un "void ImageReady"

entonces voy a disponer de esos métodos, regreso a la actividad principal, y entonces en "onException" lo que voy hacer
es mandar a llamar a "Presenter.ImageError" y le envío "e.getLocalizedMessage" y "onResourceReady"
me va permitir llamar a "presenter.ImageReady"

TODA LA LÓGICA, va estar dentro del presentador


    private void setupImageLoading() {
        RequestListener glideRequestListener = new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                presenter.imageError(e.getLocalizedMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                presenter.imageReady();
                return false;
            }
        };
        //imageLoader.setOnFinishedImageLoadingListener(glideRequestListener);

    }


Commit11: PresenteryEvent

Vamos a crear la implementación de RecipeMainPresenter

 -recipemain
    RecipeMainPresenter
        'RecipeMainPresenterImpl'

Al crear RecipeMainPresenterImpl tenemos los diferentes atributso que necesitamos como son:

    EventBus eventBus;      //para subscripciones
    RecipeMainView view;    //para controlar los métodos de la vista
    SaveRecipeInteractor saveInteractor; //para guardar una receta de acuerdo a nuestro interactuador
    GetNextRecipeInteractor getNextInteractor; //para traernos la siguiente receta


Implemetamos todos los métodos que usamos

    onCreate
        //me permite registrarme en EventBus
        eventBus.register(this);
    onDestroy
        //me permite deregistrarme y volver la vista nula
        eventBus.unregister();
        view=null;
    en dismissRecipe
        //verificamos si tengo una vista disponible y luego mando a llamar la animación de "dismiss" y luego va obtener el siguiente "recipe" llamando un método que voy a implementar a continuación en "getNextRecipe"
        if (view!=null){
         view.dismissAnimation();
        }
        getNextRecipe();

    en getNextRecipe
        //Verificamos si existe la vista llamamos a hideUIElements y a "ShowProgress"luego ejecutamos al interactuador
        //vemos que tenemos que ocultar los elementos de UI, en este caso los ImmageButtons
        if (view!=null){
            view.hideUIElements();
            view.showProgress();
        }
        getNextRecipeInteractor.execute();

    en saveRecipe
        //Validamos la vista existe, y llamamos a saveAnimation de la vista, ocultamos los elementos y mostrarmos el progressBar,  para luego llamar al interactuador que guarda un interactuador
        if (view!=null){
        view.saveAnimation();
            view.hideUIElements();
            view.showProgress();
        }
        saveRecipeInteractor.execute(recipe);


    en onEventMainThread
        //tenemos que estar suscritos con @Subscribe

    en getView
        this.view, devolvemos la vista que tenemos

    en imageError
        //Valido si tengo una vista disponible, y mando un mensaje a la vista, permitiéndome mostrar el error que tengo
        //veo si tengo un error al traer la imagen
        if (view!=null){
            view.onGetRecipeError(error);
        }
    en imageReady
        //Voy a ocultar, (contrario a lo anterior), es decir escondo el progressBar y luego muestro los elementos
        if (view!=null){
                    view.hideProgress();
                    view.showUIElements();
        }

    en onEventMainThread
        //Verificamos la vista disponible, luego voy a ir atraer un error a partir del evento con "getError" si este error es diferente de "null" tengo que notificarlo a la vista
         //escondo el progressBar y notifico a la vista de mi error
         // si es diferente de null, tengo que verificar el tipo de evento, luego si es RecipesMainEvent.NEXT_EVENT hago una cosa si no hago otra cosa con SAVE_EVENT
        //aquí llamo a "view.setRecipe" y le envío el "recipe" que viene en el evento y si lo que
        //tengo un "SAVE_EVENT" entonces le voy a avisar a la vista que la receta fue guardada
        //llamando al interactuador, para obtener la siguiente "receta" "getNextInteractor.execute"
        listo, entonces de forma automática, cuando una receta es salvada, le aviso y de una vez,
        inicio la ejecución de obtener la siguiente receta

        if (view!=null){
                    String error=event.getError();
                    if (error!=null){
                        view.hideProgress();
                        view.onGetRecipeError(error);
                    }else {
                        if (event.getType()==RecipeMainEvent.NEXT_EVENT){
                            view.setRecipe(event.getRecipe());
                        }else if (event.getType()==RecipeMainEvent.SAVE_EVENT) {
                            view.onRecipeSaved();
                            getNextRecipeInteractor.execute();
                        }
                    }
        }





En clase "RecipeMainEvent"
    //declaro mis campos del tipo de eventos que vamos a tener, ya que en este caso tenemos 2

        1.-cuando estoy guardando
        2.-cuando estoy teniendo la siguiente receta disponible
    El posible error si se generó alguno
    Si recibo un "recipe" porque viene una receta nueva,

Manejamos dos constantes
    "public final static int NEXT_EVENT= 0"
    "public final static int SAVE_EVENT = 1"
    y por ultimo "getter" y "setter" para todo lo que tenemos


Commit12: InteractorsImpl
Comenzmos con la implementación de los interactuadores

GetNextRecipeInteractorImpl implementa GetNextRecipeInteractor
//Creamos nuestro Repositorio y lo recibimos en el constructor,
en el método sobrecargado hago un número "Random" "new Random.nextInt" a partir del rango que tengo
disponible en el repositorio, entonces le voy a dar "RecipeMainRepository.RECIPE_RANGE"
y luego "repository.setRecipePage" con este número que genere y llamo al getNextRecipe
//la idea es generar y obtener la siguiente receta

    RecipeMainRepository repository;

    public GetNextRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        int recipePage=new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        repository.setRecipePage(recipePage);
        repository.getNextRecipe();
    }

SaveRecipeInteractorImpl implementa SaveRecipeInteractor
Tenemos un Repositorio, cereamos nuestro constructor y luego procedemos a llamar al saveRecipe del repository dejando todo listo con neustros interactuadores

    RecipeMainRepository repository;

    public SaveRecipeInteractorImpl(RecipeMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Recipe recipe) {
        repository.saveRecipe(recipe);
    }



