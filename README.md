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


Commit13 :RepositoryImpl

Implementamos el repositorio con esto ya tenemos la funcionalidad de la descarga que hemos generado

RecipeMainRepositoryImpl
    //defino un recipePage(que es a pagina de la receta)
    -int recipePage
    -EventBus eventBus //para las subscripciones
    -RecipeService //para conectar con el API de Retrofit

    Recibimos todo en el constructor, e implementamos los métodos de RecipeMainRepository


    //tenemos que escribir los métodos, para postear los eventos y su respectiva publicación
    //podemos crear un método genérico para postear el evento,con el tipo de evento, el error,y la receta
        //creamos el evento "RecipeMainEvent"
        //seteamos los datos a este evento con los parámetros recibidos
        //y posteamos el evento

    en post(String error,int type,Recipe recipe)
        RecipeMainEvent event = new RecipeMainEvent();
        event.setError=error;
        event.serType=type;
        event.setRecipe=recipe;
        eventBus.post(event);
    //para crear solo postear la receta opsteamos de la siguiente manera, mandando el evento y la receta, mandando como error el null
    en post(Recipe recipe){
        post(null,RecipeMainEvent.NEXT_EVENT,recipe)
    }
    //para postear que tuve un error podría reutilizar el método pero en el error mandarle Error recibido y como receta null, y como Evento cualquiera de los dos ya que solo necesitamos
     //el error en este caso tenemos NEXT_EVENT
    post(String error){
        post(error,RecipeMainEvent.NEXT_EVENT,null);
    }


    //por ultimo tenemos saveEvent  que no recibe ningun parametro, mandando como null en los parametros exepto en el tipo ya que usamos SAVE_EVENT

    post(){
        post(null,RecipeMainEvent.SAVE_EVENT,null);
    }

    En "setRecipePage" es un método sencillo y lo que hacemos es tomar el recipePage igualarlo a la variable de clase
    //con esto ya tenemos lista la asignación
    @Override
    public void setRecipePage(int recipePage) {
        this.recipePage = recipePage;
    }

    En "SaveRecipe" que igual es bastante sencillo, aquí nos vamos a apoyar bastante en el "ORM" y
    todo lo que necesito hacer es ".save" listo quedo guardado, entonces publicamos un evento
    de que ya se guardó.

    @Override
    public void saveRecipe(Recipe recipe) {
        recipe.save();
        post();
    }

    //regresamos a la lógica un poco mas pesada que es en getNextRecipe

    entonces vamos a colocar aquí, la llamada, para hacer la petición a "Retrofit" necesitamos
    un objeto "Call" le vamos a llamar "call" y lo vamos a obtener a partir del servicio
    entonces hacemos "service." y aquí esta nuestro método definido "service"
    y aquí van a ir los parámetros que yo definí:
        entonces en "BuildConfig" va estar el "FOOD_API_KEY"
        luego tengo que enviar el "sort" que eso está en "RecipeMainRepository" se llama "RECENT_SORT"
        luego tengo que enviarle el conteo "COUNT"
        luego tengo que enviarle la página que va ser el "RecipePage"
Tengo completa la llamada, a continuación tengo que hacer
un "enquere" de una respuesta, entonces vamos a definir aquí un "callback" "RecipeSearchResponse"
eso es para una llamada síncrona, eso es importante, vamos a importar el "callBack"
de "Retrofit" y a este le vamos a llamar "CallBack" nada más, y vamos a instanciarlo, tiene ciertos
métodos, "onResponse" "onFailure" y lo que vamos a hacer al final, es "call.enqueue"
esto es importante porque "Retrofit" tiene dos tipos de ejecución, síncrona y asíncrona,
en "Retrofit2" la ejecución síncrona, es decir en el mismo "Thred" es con el método
"execute" pero la ejecución asíncrona es decir con new Thread con "enqueue" es decir, esto va generar
otro "Thred" y vamos a colocar aquí, el "callback" que definimos, luego tenemos dos casos "onResponse"

"onFailure" si fallo, entonces vamos a tener un error, aquí vamos a decir "post" y queremos
enviar un error ".getLocalizerMessage"

"onResponse" vamos a hacer varias validaciones,
la primera es "response.IsSuccess" es decir, si tuvimos éxito, vamos a hacer algo, de
lo contrario, entonces hacemos un "post" del error y este error va estar en la respuestas
de "response.message" y vamos a estar reportando que ocurrió un error, si vamos a ver el error,
perdón el método "post" estamos asumiendo que es un "NextEvent" no es este "NextEvent"
y el error lo colocamos en el evento, entonces de la respuesta vamos a obtener que fue lo
que salio mal, ahora si la respuesta es exitosa, vamos a declarar un "RecipeSearchResponse"
a partir de la respuesta y el cuerpo que recibimos, entonces lo que va ser "Retrofit" usando "Gson"
es parciarlo y devolvernos un resultado, entonces voy a validar si esta respuesta con el método
"getCount" me devuelve cero, quiere decir que como le mande un número "random" el "API"
no está encontrándolo, ósea la petición se hizo de forma exitosa, me dio un resultado,
pero no encontró una receta correspondiente al "id" que el envío,
 entonces lo que hacemos en este caso, es llamar a nuestro mismo método
"setRecipePage" con un "new Random().nextInt()" y le envío aquí, el "RECIPE_RANGE", para
generar un nuevo número aleatorio y vuelvo a llamar al mismo método, de tal forma que
cuando haya una petición que no devolvió nada, vuelva a llamar al mismo método, esperando
que la siguiente si devuelva, recuerden esto va depender mucho del rango, pero como no
conozco el "API", en realidad no está, dándome un acceso a un receta aleatoria, estoy haciendo
un "WorkOfRam" para que el usuario no tenga una mala experiencia, y el "API" responda
a algo, ok, entonces si el resultado es igual a cero, hacemos esto, de lo contrario, entonces,
si tengo algo de respuesta, voy a construir un "recipe" a partir del "recipeSearch" con
"getFirstRecipe" por alguna razón, puede ser que sea "null" entonces sí, es diferente
de "null" voy a validar eso, si es diferente de "null" entonces vamos a publicar ese "recipe"
a través del evento y si es "null" por alguna razón, porque algo salio mal, ósea tengo
el resultado pero es nulo, entonces vamos a reportar un error, indicando también el
"response.message" con esto tengo listo el repositorio, estoy haciendo la petición del
"API" y mi siguiente paso, es realizar la inyección de depen dencias, para poder probar,
todo el "Stack" de mi arquitectura.

    @Override
    public void getNextRecipe() {
        Call<RecipeSearchResponse> call = recipeService.search(BuildConfig.FOOD_API_KEY, RECENT_SORT, COUNT, recipePage);
        Callback<RecipeSearchResponse> callback=new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                if (response.isSuccessful()) {
                    RecipeSearchResponse recipeSearchResponse = response.body();
                    if (recipeSearchResponse.getCount() == 0){
                        setRecipePage(new Random().nextInt(RECIPE_RANGE));
                        getNextRecipe();
                    } else {
                        Recipe recipe = recipeSearchResponse.getFirstRecipe();
                        if (recipe != null) {
                            post(recipe);
                        } else {
                            post(response.message());
                        }
                    }
                } else {
                    post(response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                post(t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
        // o podemos hacr call.enqueque(new Callback<SearchResponse> ...
    }




Commit14: MainInyection

Seguimos con la parte de inyección de dependencias de nuestro recipemain, con lo que procedemos a crear las respectivas clases y sus implementaciones dentro de la aplicacion

-di
    -RecipeMainModule
    -RecipeMainComponent


Interfaz RecipeMainComponent
    Va especificar el "API" con el que puedo hacer la inyección, entonces debemos agregar
    aquí "Singleton" "@Component" tambien para indicar la anotación y le voy a especificar que módulos va utilizar
    como son los de "RecipeMainModule" y  "LibsModule" con esto estoy
    haciendo uso de lo que la librerías van a inyectar y aquí si recuerdan que habíamos hablado
    que habían dos formas de inyectar
    1: por el método "inject" indicando cual es el "target" en este caso el "target" seria "RecipeMainActivity" y este lo habemos usado en el caso anterior,
    2: sin embargo ahora lo vamos a usar la otra forma, vamos a usar un método que le llamamos "get" para el objeto que quiero inyectar y devuelvo el objeto, recuerden aquí vamos
    a tener dos inyecciones que van a irse en cascada el "ImageLoader" y el "MainPresenter", "ImageLoader" viene de las librerías y el "MainPresenter" va venir del "MainModule",
    Tento que tener en mi Module los metodos que tengan el  método "provides" que devuelve exactamente este objeto, entonces nos vamos
    a mover ahora al módulo y comenzamos trabajando

    @Singleton
    @Component(modules = {RecipeMainModule.class, LibsModule.class})
    public interface RecipeMainComponent {
        //void inject(RecipeMainActivity activity);
        ImageLoader getImageLoader();
        RecipeMainPresenter getPresenter();
    }

RecipeMainModule

    en el módulo vamos a tener un "recipeMainView" que vamos a recibir en el constructor, luego el método que provee
    la vista, es probablemente el más sencillo, "provides @Singleton" y devuelve un "recipeMainView"
    provee la vista, y es el más sencillo, porque lo que vamos a hacer es devolver el mismo
    objeto que recibimos en el constructor.
    Luego vamos a tener un método similar pero para el presentador y usamos este mismo para el nombre del método, y entonces ¿qué necesita
    el presentador? vamos a ver la clase, vamos a ver que tiene el "MainPresenterImplementation"
    y tiene "eventBus" "View" e "Interactors" entonces le agregamos por aquí en el módulo
    que reciba todo eso como parámetros en el método y devolvemos
    un "new RecipeMainPresenterImplementation" que va usar exactamente esos parámetros,

    @Module
    public class RecipeMainModule {
        RecipeMainView view;

        public RecipeMainModule(RecipeMainView view) {
                this.view = view;
            }
            @Provides @Singleton
            RecipeMainView provideRecipeMainView() {
                return this.view;
            }

            @Provides
            @Singleton
            RecipeMainPresenter provideRecipeMainPresenter(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor save, GetNextRecipeInteractor getNext) {
                return new RecipeMainPresenterImpl(eventBus, view, save, getNext);
            }

            @Provides @Singleton
            SaveRecipeInteractor provideSaveRecipeInteractor(RecipeMainRepository repository) {
                return new SaveRecipeInteractorImpl(repository);
            }

            @Provides @Singleton
            GetNextRecipeInteractor provideGetNextRecipeInteractor(RecipeMainRepository repository) {
                return new GetNextRecipeInteractorImpl(repository);
            }

            @Provides @Singleton
            RecipeMainRepository provideRecipeMainRepository(EventBus eventBus, RecipeService service) {
                return new RecipeMainRepositoryImpl(eventBus, service);
            }

            @Provides
            @Singleton
            RecipeService provideRecipeService() {
                RecipeClient client = new RecipeClient();
                return client.getRecipeService();
    }

    Luego después del presentador, vamos a necesitar proveer, los dos interactuadores, vamos a
    necesitar proveer, un "saveRecipeInteractor" que va recibir, vamos a verlo, el "SaveRecipeInteractorImpl"
    recibe un repositorio,etnocnes mandamos los respectivos atributos desde su constructor
    el "SaveRecipeInteractor" tiene un método asociado con un nombre correspondiente y vamos
    a devolver una instancia que va recibir el repositorio, de una forma similar vamos a
    trabajar, un "getNextRecipeInteractor" corregimos en el objeto
    que estamos devolviendo y están los interactuadores listos

    El siguiente es el repositorio, me aparece un poco el método, copiamos lo que va devolver y que vamos a devolver una implementación
    del mismo y se provee desde aquí, ahora que tendrá, este "RecipeMainRepositoreImpl" que
    tendrá de parámetros, tiene un "RecipePage" lo estoy setiando, entonces lo vamos a borrar
    del constructor para que no tenga que indicar nada de entrada y vamos a utilizar únicamente
    estos dos parámetros, el "eventBus" y el servicio, entonces esos dos los colocamos
    aquí, como parámetros del método que los provee, y vamos a utilizarlos también para
    instanciar los objetos, "eventBus" viene de las librerías,
    Me hace falta únicamente el servicio que tiene que ver con "Retrofit" esto podría estar también en
    las librerías, sin embargo como no lo estoy utilizando en otro modulo que no sea en este,
    lo voy a colocar aquí, es importante esa aclaración, porque lo estoy utilizando únicamente
    en este "MainModule" recordemos mi aplicación ahorita tiene dos "Features" es una aplicación
    muy pequeñita, por eso puedo hacer este tipo de cosas, vamos a hacer un "Provides Recipe Services"
    y este no recibe ningún parámetro y lo que vamos a hacer es devolver un "New  RecipeClient.getRecipeService"

Si quisiera hacer algún tipo de "Testing" enfocado
    en esto de nuevo tengo que hacer el servicio de una forma diferente y lo vamos a platicar
    cuando lleguemos al módulo de testing y además debería inyectar también aquí el cliente,
    para poder reemplazarlos en algún momento





En "ApplicationClass" es un "FacebookRecipeApp.Java" lo vamos a
agregar aquí un método, que se llame, "public RecipeMainComponent getRecipeMainComponent"
que va recibir un "RecipiMainActivity" y un "RecipiMainView" para poder construir los
módulos necesarios, que vamos a devolver un "daggerRecipeMainComponent.builder"
Tener cuidado con el "RequestManager" está usando un fragmento yo no quiero usar un fragmento, yo quiero usar una actividad,
entonces agregamos lo necesario para usar esta actividad con Glide, agregamos aquí que va hacerse uso de esa actividad y regresamos
nuevamente a mi "ApplicationClass"



    public RecipeMainComponent getRecipeMainComponent(RecipeMainActivity activity, RecipeMainView view) {
        return DaggerRecipeMainComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .recipeMainModule(new RecipeMainModule(view))
                .build();
    }


Seguimos construyendo mi "builder" sin embargo previo a darle
"Build" voy a necesitar un "LibsModule new LibsModule" y le indico aquí la actividad,
y luego un "RecipeMainModule new RecipeMainModule" y le indico aquí la vista, con esto tengo
mi "get MainComponent" entonces puedo moverme hacia la actividad, "RecipeMainActivity" listo,



En RecipeMainActivity
Agrego aquí la inyección, esta inyección entonces vamos a colocar "private RecipeMainComponent component" y este componente me va permitir a mi hacer la inyección entonces nos vamos
a "SetupInjection" con su respectivo Cast y sobre "App" hago un "get RecipeMainComponent"
de hecho esto podría, asignarlo, tengo que asignarlo, "component = app.getRecipeMainComponent"
y le tengo que enviar los parámetros necesarios "this, this" una actividad y una vista, que
son los dos al mismo objeto, luego sobre el componente, lo que haría es, "Inject this"
en este caso no lo tengo porque no está el método, pero el problema con esto es que
estoy encerrando esta inyección y necesitaría otra inyección cuando quiero hacer "testing"
entonces un "worker ron´s" relativamente sencillo es poner "imageLoader = get ImageLoader"
y "Presenter = getPresenter" y estos métodos los vamos a crear aquí abajito, y en vez
de devolver el objeto que definimos, vamos a hacer "component.getImageLoader" vamos a
crear el otro, donde está el "get Presenter" listo, le vamos a poner aquí "component.getPresenter"
funciona igual y cuando quiera hacer el test de esta actividad voy a poder sobrecargar
estos dos métodos y proveer "MOCS" buscamos la parte de "ImageLoading" vamos donde está
aquí, y ahora ya no va ser nulo, entonces ya le podemos poner, que ejecute el presentador,
cuando sea necesario y vamos a ver el resultado, vamos a ver el emulador y aquí tenemos nuestra
receta, en este caso yo la quiero conservar, se guardó exitosamente, y esta no la quiero
conservar, y entonces me va a traer otra, la imagen todavía se ve, no tengo el gesto
de arrastrar, esto es lo que voy a implementar a continuación.

    private RecipeMainComponent component;
    //....
    private void setupInjection() {
        FacebookRecipesApp app = (FacebookRecipesApp)getApplication();
        //app.getRecipeMainComponent(this, this).inject(this);
        component = app.getRecipeMainComponent(this, this);
        imageLoader = getImageLoader();
        presenter = getPresenter();
    }
    public ImageLoader getImageLoader() {
        return component.getImageLoader();
    }

    public RecipeMainPresenter getPresenter() {
        return component.getPresenter();
    }

Issue
    Tenemso un error de login de facebook y no se sabe que si ya nos logeamos y dimos permisos y configuramos en developers.facebook
    por lo que eliminamos la app y creamos otra como la anterior y luego creamos las claves de esta
    de acuero a este articulo
https://stackoverflow.com/questions/20301025/facebook-key-hash-does-not-match-any-stored-key-hashes

Issue
    TEnemso este error
    Unable to start activity ComponentInfo{ec.edu.lexus.facebookrecipies/ec.edu.lexus.facebookrecipies.recipemain.ui.RecipeMainActivity}: java.lang.ClassCastException: android.app.Application cannot be cast
    y es porque no esta definida en el manifiesto nuestra clase app


Commit15 :Animation_Swipe

Comenzamos con las animaciones que veremos en nuestro proyecto por lo que usamos lo siguiente

recipemain
    -ui
        -SwipeGestureDetector(Class)
        -SwipeGestureListener(Interface)


    En la interface
         "onKeep" y "onDismiss" listo,
            void onKeep();
            void onDismiss();


    En la clase definimos un par de constantes, para el manejo de "swipe"
    cuál es el "SWIPE_THRESHOLD" que queremos superar y que velocidad, vamos a tener también como un límite, como un "THRESHOLD" y vamos a usar una interfaz, llamada: "SwipeGestureListener"
    para saber qué hacer en el momento que se dio el "swipe" en alguna dirección


    private SwipeGestureListener listener;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    Lo recibimos dentro del constructor a la interface  y vamos a
    sobrecargar un par de métodos nos interesa sobrecargar
            "onDown"
                y aquí devolveremos true" si ocurrió el evento
            "onFling"
                que aquí recibe par de "MotionEvent" recordemos que "swipe" va requerir de estos
````````````````dos eventos, cuando hay un "down" un "move" y luego un "Up" pero tienen que tener un "match"
                entonces aquí en "onFling" es donde vamos a hacer la implementación


                Aquí en "onFling" vamos a hacer un "float" que refleje la diferencia entre los dos eventos
                "e2.getY() - e1.getY()" lo mismo para la "X" en este caso lo que cambia es el método que
                vamos a llamar
                condicionamos, si el valor absoluto, recuerden que podemos
                tener valores positivos y negativos, de la diferencia de X, es mayor que el valor absoluto,
                de la diferencia de Y, entonces verificamos que si el valor absoluto, de la diferencia
                de X es mayor que el "THRESHOLD" y además el valor absoluto de la variable de velocidad
                en X es mayor que el "SWIPE_VELOCITY_THRESHOLD" que tenemos para la velocidad, si esto se cumple vamos a revisar, si la diferencia de X es mayor que 0, entonces
                esto significa, que vamos a llamar al método "onKeep" porque es un "swipe" hacia la derecha,

                de lo contrario vamos a llamar al método "onDismiss" porque es un "swipe" hacia la
                izquierda, de hecho podríamos hacer,


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {//esto es un swipe hacia la derecha
                        listener.onKeep();
                    } else {//y sino es un swipe a la izquierda
                        listener.onDismiss();
                    }
                }
            }
            result = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }


Procedemos al "MainActivity" vamos a abrir el
"RecipeMainActivity" y aquí vamos a hacer un par de cosas vamos a agregar


        "setupGestureDetection"
                    y en este método, vamos a poner lo necesario, para la detección, que ¿necesitamos?, necesitamos
                    crear un "GestureDetector" con el nombre y este "GestureDetector" va recibir, ciertos
                    parámetros, enviamos "this, new SwipeGestureDetector" que veamos "this on Listener" le ponemos "this"
                    nos da un error, hacemos que, nuestra actividad implemente este "listener" por lo tanto voy
                    a necesitar métodos, "onKeep" y "onDismiss" estos métodos ya los tenia, lo único que
                    necesito, es que además, sobrecarguen a los métodos de la interfaz, entonces le agrego
                    la anotación "override" a los metodos de la acitvidad como son onKeep y onDismiss


                private void setupGestureDetection() {
                    final GestureDetector gestureDetector = new GestureDetector(this, new SwipeGestureDetector(this));
                    View.OnTouchListener gestureListener = new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            return gestureDetector.onTouchEvent(event);
                        }
                    };
                    imgRecipe.setOnTouchListener(gestureListener);
                }
                 "GestureDetector"

                    Hacemos un "view.OnTouchListener gestureListener" con el nombre "new" etcétera, tenemos un
                    método "onTouch" aquí, vamos a en vez, de "return" falso vamos a devolver "gestureDetector.OnTouchEvent"
                    y le enviamos el mismo evento "motionEvent" y luego esto lo vamos a asignar a la imagen,
                    "ImageRecipe.setOnTouchListener" y le enviamos este, veamos, "GestureListener" le voy a hacer
                    "GestureOnTouchListener" necesitamos también que
                    la imagen se anime, y que la imagen, se limpie a la hora de que se está cargando la otra,

        "clearImage"
                para limpiarla y esto va ir asociada con la
                animación, hacemos "imageRecipe.setImageResource" le vamos a enviar un recurso cero, para que
                no muestre nada.

                private void clearImage(){
                    imgRecipe.setImageResource(0);
                }

        "saveAnimation"
                Declaramos un "Animation anim = animationUtils.loadanimation(getApplicationContext)" necesito un contexto y una animación, este
                es un archivo "XML" le ponemos "R.anim.save_animation" este todavía no existe, entonces vamos a
                crear este "ResourceFile" le llamamos "save_animation" y aquí lo que vamos a tener es una animación
                con ciertas características para que se vaya hacia la derecha ósea es desde el centro,
                hacia la derecha, de la misma forma

                @Override
                public void saveAnimation() {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_save);
                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            clearImage();
                        }
                    });
                    imgRecipe.startAnimation(anim);
                }
        "dismissAnimation"

                Hacemos lo mismo para el "dimiss" pero en este caso le llamamos "dismissAnimation" creamos este archivo y
                la diferencia con la otra animación es que esta tiene a menos cien por ciento va hacia
                el otro lado, la duración es de "350 milisegundos" "700 milisegundos", luego de tener la animación, vamos a tener un, "ImageRecipe.StartAnimation"
                y le envío mi objeto de animación, lo mismo en los dos casos, sin embargo me interesa
                cuando termine la animación, la imagen se borre, para eso voy a usar un "animationListener"
                vamos hacer un método privado, que me puede ayudar con esto "private animationListener getAnimationListener" y en este método vamos a hacer "return new AnimationListener" tiene
                tres métodos "onAnimationStart" "onAnimationEnd" "onAnimationRepeat" nos interesa que cuando
                termine, se limpie la imagen, entonces hacemos aquí un "clearImage" y a ambas animaciones
                vamos a decir "Anim.setAnimationListener" y llamamos a este método en los dos casos.package

                @Override
                public void dismissAnimation() {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_dismiss);
                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) { }

                        @Override
                        public void onAnimationRepeat(Animation animation) { }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            clearImage();
                        }
                    });
                    imgRecipe.startAnimation(anim);
                }

Para las animaciones tenemos
este es para dismiss
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:shareInterpolator="false" >
    <translate android:duration="350" android:fromXDelta="0%" android:toXDelta="-100%"/>
    <alpha android:duration="700" android:fromAlpha="1.0" android:toAlpha="0.0" />
</set>
este es para save
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:shareInterpolator="false" >
    <translate android:duration="350" android:fromXDelta="0%" android:toXDelta="100%"/>
    <alpha android:duration="700" android:fromAlpha="1.0" android:toAlpha="0.0" />
</set>




Para resumir la creacion de un gestureDetector

SwipeGestureDetector

Definimos el manejo del Swipe el SWIPE_THRESHOLD y el SWIPE_VELOCITY_THRESHOLD,
    Sobrecargamos
        onDown, si ocurrio el evento
        onFling, toma los eventos
            diferencia entre los dos eventos, para detectar los elementos

SwipeGestureListener
Definimos el listener para saber que hacer en el momento que se dio el listener en alguna dirección

RecipeMainActivity
En la actividad que lo implementa la interfaz que hemos definido, con esto nos aseguramos que se va a realizar lo que necesitmaos
hacemos un gestor de detectores y le enviamos la actividad y la nueva clase que hicismo que exitenda de gesture detector



RecipeList
Commit16: Layout_RecipeLayout_Save

    Nuestro siguiente modulo o la siguiente característica que vamos a desarrollar, es el listado de
    todo lo que hemos guardado, para este listado vamos a tener:

                un "Toolbar", personalizado sin el de por defecto
                    En la parte superior, con un "icono", que nos va llevar de regreso a la vista principal,
                     aunque podríamos simplemente presionar el botón de "Back" el resultado es el mismo,

                    Vamos a tener también la opción de "cerrar sesión"

                Es un "RecyclerView"
                        donde tenemos con vista de "grid" y tenemos dos columnas,

                en cada columna, más bien en cada elemento, vamos a tener:
                        la imagen
                        el título
                        un icono que representa si es favorito
                        un icono para borrarlo
                        si es favorito,  va estar marcado de amarillo y si no, no lo va estar,
                            en el momento de hacer click va
                            cambiar por ejemplo, si aquí hiciera click cambia a amarillo, y si aquí, hiciera click, cambia a no tener un relleno,

                        el botón de eliminar permite borrar ese registro de la
                            base de datos y todo esto, todos estos registros vienen de la base de datos, entonces aunque
                            no haya seleccionado nada en la selección actual, están allí guardados, y también
                            tengo para compartir y para enviar, en este caso, algo que quiero hacer explícito es
                            que esos botones son de "Facebook" entonces dependen mucho del contenido y la implementación
                            de "Facebook" en el caso de ambos tiene que tener el contenido, configurado para que estén
                            habilitados, y en el caso de "send" tiene que tener instalado el usuario y la aplicación
                            de "FacebookMessenger" para que este habilitado

                Además en el "ToolBar" vamos a poder hacer un click para que me lleve a la parte superior
                del listado, entonces puedo hacer un Scroll el "ToolBar" se oculta, pero cuando le hago
                click me lleva hasta la parte superior de este listado



    Empecemos con esto, voy a necesitar dos "Layout" uno para la actividad y uno para cada uno de los elementos de este
    listado, nuestro "Layout" va funcionar con "ToolBar" entonces por el momento, necesitamos
    trabajar tanto en el "manifest" como en "styles.xml"



    En "styles" vamos a agregar los estilos
    para que el tema no tenga "ActionBar"

     <style name="AppTheme.NoActionBar">
             <item name="windowActionBar">false</item>
             <item name="windowNoTitle">true</item>
     </style>
     <style name="AppTheme.AppBarOverlay" parent="ThemeOverlay.AppCompat.Dark.ActionBar" />
     <style name="AppTheme.PopupOverlay" parent="ThemeOverlay.AppCompat.Light" />


    En el "manifest" vamos a indicarle, que la actividad
        de "RecipeListActivity" tiene un "android:theme" "appTheme.NoActionBar"

        <activity android:name=".recipelist.ui.RecipeListActivity"
            android:theme="@style/AppTheme.NoActionBar" />
        <activity




    Al "Layout" entonces abrimos el "RecipeList" y aquí vamos a colocar
            Nuestro listado,
            Vamos a colocar el "CordinatorLayout" y lo vamos a colocar un "toolBar"

            Tenemos una "AppBarLayout" dentro de la "AppBarLayout" tenemos un "ToolBar" todo esto está dentro del "coordinatorLayout"

             <android.support.design.widget.AppBarLayout
                    android:id="@+id/appbar"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:paddingTop="@dimen/appbar_padding_top"
                    android:theme="@style/AppTheme.AppBarOverlay">

                    <android.support.v7.widget.Toolbar
                        android:id="@+id/toolbar"
                        android:layout_width="match_parent"
                        android:layout_height="?attr/actionBarSize"
                        android:background="?attr/colorPrimary"
                        app:layout_scrollFlags="scroll|enterAlways"
                        app:popupTheme="@style/AppTheme.PopupOverlay">

                </android.support.v7.widget.Toolbar>

            Y además hay un "RecyclerView" que va tener
            interacción al hacer "Scroll" con los demás componentes

                    <android.support.v7.widget.RecyclerView
                        android:scrollbars="vertical"
                        android:id="@+id/recyclerView"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>


                </android.support.design.widget.CoordinatorLayout>




    Tenemos nuestro "ToolBar" recordemos que no tiene "ActionBar" y ahora
    podemos implementar, el "Layout" de cada uno de los elementos, a este le vamos a llamar,"element_stored_recipes.xml"
    dentro de el mismo, tenemos:

            Un "ImageView" con cierto tamaño, vamos a crear este, ese tamaño de la imagen, le ponemos "150DP" un "TextView" para mostrare el título de la
            receta.


            Un "LinearLayout" que tiene dos "imageButtons" que tienen que tener cierto
            "Padding" le vamos a poner "8dp"


            Aparte de estos iconos, vamos a agregar los botones
            de "Facebook" primero vamos a ver como luce en este momento, tenemos la imagen de un tamaño
            fijo de "150x150" el título, la estrella para indicar que lo puedo volver favorito
            o no, y el bote de basura para indicar que lo puedo eliminar


    <ImageView
        android:layout_width="@dimen/recipes_list_image_size"
        android:layout_height="@dimen/recipes_list_image_size"
        android:id="@+id/imgRecipe"
        android:src="@mipmap/ic_launcher"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceMedium"
        android:text="Medium Text"
        android:layout_weight="1"
        android:padding="@dimen/activity_horizontal_margin"
        android:id="@+id/txtRecipeName"
        android:layout_below="@+id/imgRecipe"
        android:layout_centerHorizontal="true" />

    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/txtRecipeName"
        android:id="@+id/layoutButtons"
        android:layout_centerHorizontal="true">
        <ImageButton
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@android:drawable/btn_star_big_off"
            android:padding="@dimen/row_icons_padding"
            android:background="@android:color/transparent"
            android:id="@+id/imgFav" />

        <ImageButton
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@android:drawable/ic_menu_delete"
            android:padding="@dimen/row_icons_padding"
            android:tint="@android:color/darker_gray"
            android:background="@android:color/transparent"
            android:id="@+id/imgDelete" />

    </LinearLayout>


            Agrego los elementos de "Facebook"
                fuera del "LinearLayout" vamos a agregar un
                    "sharedButton" que va tener "wrap_content" para ancho y alto y también vamos agregar
                    "sendButton" que va tener "wrap_content" de ancho y alto, además le vamos a poner,



    <com.facebook.share.widget.ShareButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/row_icons_padding"
        android:id="@+id/fbShare"
        android:layout_below="@+id/layoutButtons"
        android:layout_centerHorizontal="true" />

    <com.facebook.share.widget.SendButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/row_icons_padding"
        android:id="@+id/fbSend"
        android:layout_below="@+id/fbShare"
        android:layout_centerHorizontal="true" />

    Estamos dando un layout a nuestras interfaces hechas dentro
    en este caso no va ser un, "padding" sino va ser un "margin" y lo vamos a poner al rededor,
    completo del elemento, en los dos y vamos a agregar un identificador a cada uno, este
    va ser "fbshared" y este otro va ser "fbsend" para indicar que es de el "messenger" para
    enviarlo, luego esto va estar abajo, "below" de "share" "id/fbshared" y va estar centrado
    horizontalmente "centerHorizontal="true" ", este primero va estar también centrado horizontalmente
    y va estar abajo del "Layout" entonces este se llama "LayoutButtons", si es este identificador
    que ya tenía, vamos a ver como luce, está aquí uno y aquí el otro, no los veo, necesito
    para renderizarlo, poderlo visualizar directamente, en el dispositivo ejecutándose.



Commit17: Structure MVP RecipeList


Comenzamos a desarrollar la estructura MVP para usarla por lo que definimos nuevo paquetes quedando de la siguiente manera

-recipelist
    -events
        RecipeListEvent
    -ui
        RecipeListView
        RecipeListInteractor
        RecipeListRepository
        RecipeListPresenter
        StoredRecipesInteractor

Explicación

    En RecipeListView
        Entonces voy a tener un listado y ese listado, puede ser actualizado, cuando
        presionan sobre favoritos, o puede ser que borre un elemento, entonces cuando ocurre
        eso, tengo que borrarlo también del listado, entonces vamos a poner

            "void setRecipesList(recipe)  data" cuando recibo los datos inicialmente
            "recipeUpdate" cuando estoy modificando un favorito
            "recipeDelete" cuando estoy eliminando "recipe" algún elemento,


        public interface RecipeListView {
            void setRecipes(List<Recipe> data);
            void recipeUpdated();
            void recipeDeleted(Recipe recipe);

    En RecipeListPresenter

    Aquí tenemos
        "onCreate" "onDestroy", para registrar y deregistrar además de devolver la vista nula
        "getRecipes" para obtener que tengo en la base de datos
        "removeRecipe" cuando el usuario presiona que quiere borrarlo
        "void toggleFavorite" que recibe también un "recipe"
        "RecipeListView" con objetivos de "testing" al igual que lo hicimos en el otro modulo
        "onEvent(MainThread)" que recibe un "recipeListEvent"


    En Interactuadores
        podría haber hecho tres interactuadores,
        se me hizo mucho, lo voy a dejar en dos, uno para obtener, uno que lee, y otro escribe
        a la base de datos, actualizando o eliminando alguno de los elementos

        Interactuador "recipeListInteractor"
            que este va ir a traer los elementos(Leer)le llamamos "void execute" a su método

        Interactuador "StoreRecipeInteractor",
            la diferencia va ser que este, ambos están
            actuando sobre lo que está guardado
                "execute", para actualizar,
                "update" recibe un "recipe"
                 "executeDelete" , por ultimo vamos


Commit19
            :Config_Adapter

        procedemos a trabjar en el adapters porl o que usamos la estructura
        -recipelist
            -ui
                -adapters
                    -onItemClickListener(interface)




        En onItemClickListener


            Vamos a implementar a que le puedo hacer "clic"

            Un "Favorite" como onFavClick tengo que recibir el objeto "recipe"

            Un "delete", tengo que recibir también el objeto "recipe"

            Un "itemClick" para poder hacer click sobre el elemento sobre tal que también recibe el objeto

            public interface OnItemClickListener {
                void onFavClick(Recipe recipe);
                void onItemClick(Recipe recipe);
                void onDeleteClick(Recipe recipe);

            }

        Puedo hacer clic, también sobre los botones de "Facebook" pero como
        tiene un manejo ligeramente diferente diferente, no los voy a colocar aquí en mi interface,
        sino los voy a trabajar directamente desde el adaptador.






Comenzamos creando el recipes Adapter
        Con los lineamentos que ya hemos seguido pues comenzamos a realizarlos


        Vamos a tener
            "private List" "recipeList" para manejar mi "dataSet"
            "private ImageLoader" para cargar las imágenes
            "private OnItemClickListener" "ClickListener" que obtenemos todo a través del constructor

    private List<Recipe> recipeList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

        Ya que tengo este "RecipeList" definido, vamos aquí a devolver el tamaño
        y vamos a agregar un par de métodos que me ayudan, para el mantenimiento es decir agregar
        o quitar,


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_stored_recipes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        imageLoader.load(holder.imgRecipe,currentRecipe.getImageURL());
        holder.txtRecipeName.setText(currentRecipe.getTitle());
        holder.imgFav.setTag(currentRecipe.isFavorite());//perimite poner un objeto asociado a un lemento, en este caso el elemento es el bot'on de favorito, con esto voy a ver a la hora de hacer la prueba eque valor tiene el HOLDER
        if (currentRecipe.isFavorite()){
            holder.imgFav.setImageResource(android.R.drawable.btn_star_big_on);
        }else   holder.imgFav.setImageResource(android.R.drawable.btn_star_big_off);

        holder.setOnItemClickListener(currentRecipe,onItemClickListener);
    }


        lo vamos a tener
                "public void setRecipes" que recibe una lista de "recipe"
                y lo que voy a hacer asignarla, "this.recipeList = Recipes" y notifico que cambiaron los datos,

                  public void setRecipes(List<Recipe> recipe) {
                        this.recipeList = recipe;
                        notifyDataSetChanged();
                    }

                "void removeRecipe(Recipe recipe)" este es para borrar, entonces cuando borro
                vamos a ver si "recipeList.remove(recipe)" y quito el objeto, recordemos que en "recipe"
                implemente un método "Equals" que me va ayudar para esto y también tengo que notificar el
                cambio de datos

                 public void removeRecipe(Recipe recipe) {
                    recipeList.remove(recipe);
                    notifyDataSetChanged();
                }


                En "onCreateViewHolder" aquí vamos a inflar, a partir del "Layout"
                que definí, es ese "element_stored_recipes" y voy a devolver un "new ViewHolder" a partir
                de esta vista


                En "onBindViewHolder" aquí vamos a obtener cual es la receta actual
                "CurrentRecipe" ahora sí, "recipeList.get" en base a la posición que estoy recibiendo
                como un parámetro y que vamos a hacer, vamos a cargar con el "ImageLoader" utilizando un
                "ImageView" que está en el "Holder" "ImageRecipe" todavía no la tengo, vamos a crear eso, entonces
                voy a darle aquí, que me haga una inyección de "ButterKnife" generando a partir de este "Layout" lo va poner en el
                "Adapter" y lo voy a mover al "ViewHolder"
                        tengo un "ImageView"
                        un "TextView"
                        dos "ImageButton"
                        el "Layout" no me interesa,
                        "share" y el "send" confirmamos y solo lo vamos a cortar de aquí
                        y a pegar en el "ViewHolder"
                entonces ahora si puedo hacer "Holder.imageRecipe" y el "URL"
                lo voy a sacar del "currentRecipe" con "getImageURL" luego vamos a modificar "holder." el título
                "txtRecipeName.setText" y aquí hacemos "currentRecipe.getTitle"

                    @BindView(R.id.imgRecipe)
                    ImageView imgRecipe;
                    @BindView(R.id.txtRecipeName)
                    TextView txtRecipeName;
                    @BindView(R.id.imgFav)
                    ImageButton imgFav;
                    @BindView(R.id.imgDelete)
                    ImageButton imgDelete;
                    @BindView(R.id.fbShare)
                    ShareButton fbShare;
                    @BindView(R.id.fbSend)
                    SendButton fbSend;


                Ahora vamos a poner también la imagen de
                favorito, pero esto lo vamos a condicionar con el objetivo de poder testear, que imagen
                es la que se está mostrando, vamos a aquí a usar el "tag" permite poner
                un objeto asociado a un "elemento" en este caso el elemento, es, el botón de favorito
                y lo que le vamos a poner es "currentRecipe.getFavorite" entonces con eso voy a poder saber, a la hora
                de hacer la prueba, qué valor tiene el "holder" y voy asumir que ese "tag" guardado sobre
                la imagen, es precisamente el recurso que está mostrando, entonces vamos a condicionar
                aquí,

                Que si el "currentRecipen.getFavorite" es verdadero, entonces vamos a cambiar el
                recurso, "holder.ImageFav.setImageResource" entonces ya le puedo decir "android.R.Drawable"
                ósea
                    si es un favorito, "btn_star_big_on" de lo contrario, que haga lo mismo, pero con
                    "big_off" entonces a través de este "tag" voy a poder probar más adelante si está
                    cambiando o no el recurso

                por último hacemos un "holder.set" tampoco existe el método,
                vamos hacerlo "setOnItemClickListener" y le envío el "currentRecipe" y le envío el "onItemClickListener"
                que tengo por aquí, como no existe vamos a crearlo y estamos listos, ahora a trabajar
                en el "ViewHolder".


        Entonces en el "ViewHolder" vamos a definir un "private View" aquí que
        vamos a asignar en el constructor a "ItemView" y también vamos hacer la inyección de "ButterKnife"
        no está en otro lado, no no la tengo "ButterKnife.bind(this, view)" estoy utilizando el elemento que acabo
        de asignar y en el método "setOnItemClickListener" ambos tienen que ser finales, porque los vamos
        a trabajar desde una clase interna vamos a decir "view.setOnClickListener" instanciamos
        un "onItemClickListener" "Listener" no se llama "onItemClick.onItemClick" y necesito
        enviarle el "currentRecipe" esto para el "Click" en el elemento general, voy a tener que hacer
        algo similar para cuando es favorito "imageFav.setOnClickListener(new View)" etcétera
        Aquí voy a decir "onItemClickListener.OnFavClick" y le envío también el "(currentRecipe)"
        lo mismo para cuando se borra, "setOnClickListener(new  ViewOnClickListener)" etcétera y llamo al "onItemClickListener.onDeleteClick" y le envío
        también el "currentRecipe" eso es para los tres elementos que yo voy a estar manejando

 public void setOnItemClickListener(final Recipe currentRecipe, final OnItemClickListener onItemClickListener) {
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(currentRecipe);
                }
            });
            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(currentRecipe);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDeleteClick(currentRecipe);
                }
            });


        En el caso de los botones de "Facebook" necesito ponerle un contenido o me van a aparecer
        deshabilitados, entonces voy a instanciar aquí un "ShareLinkContent" este es de "Facebook"
        le llamamos "content" "new ShareLinkContent.Builder" y sobre este sobre este vamos hacer "setContentUrl" noten que tengo otras cosas
        que le podría tener, la descripción, el título, el URL, de hecho podría ponerle
        las tres, pero el que más me interesa en este caso es el URL, para poder decir "Url.parse."
        "currentRecipe.getSourceURL" y por ultimo "Build"

        entonces ya tengo el
        contenido y ahora lo tengo que asignar puedo hacer "fbShare.setShareContent" y le envío este contenido, lo mismo con "fbSend.SetShareContent.setShareContent (content)"

        Con esto nuestro adaptador está listo, ya tenemos asignado los "Click",
        ya tenemos asignado los "render" que es lo que se muestra y podemos continuar con el
        siguiente paso de la implementación.

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(currentRecipe.getSourceURL()))
                    //.setQuote()
                    .build();
            fbShare.setShareContent(content);
            fbSend.setShareContent(content);






Commit20

        :Recipe_List_Activity_Setups

Vamos a configurar la actividad para implemetar los setups dentro de esta, cmo lo ponemos:

        -Butterknife.bind(this), con RecyclerView, Toolbar
        -setupToolbar
        -setupRecyclerView
        -setupInjection


Con esto podemos trabjar con la implementación del presentador

        Definimos
            -"recipeAdapter"
            -"presenter"

    RecipesAdapter adapter;
    RecipeListPresenter presenter;

        Definimos la lógica

            en onCreate
                ...
                presenter.onCreate
                presenter.getRecipes


                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_recipe_list);
                    ButterKnife.bind(this);
                    setupToolbar();
                    setupRecyclerView();
                    setupInjection();
                    presenter.onCreate();
                    presenter.getRecipes();
                }
            en "setupToolbar"
                hacemos un "setSupportActionBar" "Toolbar"
                    private void setupToolbar() {
                        setSupportActionBar(toolbar);
                    }
            Además vamos a agregar un método
            "public void onToolbarClick" decorado con el identificador del "Toolbar" para que cuando
            le hagan  "onClick" y aquí lo que vamos hacer es "RecyclerView.smoothScrollToPosition(0)"
            para que este en la parte superior en el momento que le hacen "Click"

                    @OnClick(R.id.toolbar)
                    public void onToolbarClick(){
                        recyclerView.smoothScrollToPosition(0);
                    }

            en onDestroy

                "Presenter.onDestroy" y vamos a sobrecargar también


            "onCreateOptionsMenu"
                el menú que voy a mostrar, tanto el icono para volver a la pantalla principal, como "Logout" entonces
                ponemos aquí el contenido

                    @Override
                    public boolean onCreateOptionsMenu(Menu menu) {
                        getMenuInflater().inflate(R.menu.menu_recipes_list,menu);
                        return super.onCreateOptionsMenu(menu);
                    }

            En menu_recipes_list
                Es una parte igual a menu_recipes_main

                Vamos a ponerle "ic_menu_gallery" para que me muestre
                el dibujito adecuado y en vez de "menu.action.list" le vamos a poner "menuactionmain" aunque esto
                no se ve, siempre es una buena idea que lo tenga, le vamos a poner, elegir receta

                            <?xml version="1.0" encoding="utf-8"?>
                            <menu xmlns:android="http://schemas.android.com/apk/res/android"
                                xmlns:app="http://schemas.android.com/apk/res-auto"
                                xmlns:tools="http://schemas.android.com/tools"
                                tools:context=".recipemain.ui.RecipeMainActivity">
                                <item
                                    android:id="@+id/action_main"
                                    android:title="@string/recipemain.menu.action.main"
                                    android:icon="@android:drawable/ic_menu_gallery"
                                    app:showAsAction="always" />

                                <item
                                    android:id="@+id/action_logout"
                                    android:title="@string/global.menu.action.logout"
                                    app:showAsAction="never" />
                            </menu>
            En onOptionsItemSelected
                Cambiamos el id con actionMain para diferenciarlo de loq ue copiamos de RecipeMain
                        @Override
                        public boolean onOptionsItemSelected(MenuItem item) {
                            int id = item.getItemId();

                            if (id == R.id.action_main) {
                                navigateToMainScreen();
                                return true;
                            } else if (id == R.id.action_logout) {
                                logout();
                                return true;
                            }
                            return super.onOptionsItemSelected(item);
                        }

            En navigateToMainScreen
                lo mandamos a RecipeMainActivity


                        private void navigateToMainScreen() {
                            startActivity(new Intent(this, RecipeMainActivity.class));
                        }
            En setupRecyclerView
                vamos a poner "recyclerView.setLayoutManager(new GridLayoutManager)" y queremos dos columnas
                además le vamos a decir "setAdapter" y el adaptador que ya tenemos definido, con esto tenemos la actividad lista
                    private void setupRecyclerView() {
                        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
                        recyclerView.setAdapter(adapter);
                    }


Nos hace falta que implemente a la vista, entonces le damos "implement RecipeListView" lo cual
nos va obligar a tener tres métodos más "setRecipe" "recipeUpdate" "recipeDelete"


            En  "setRecipe"
                vamos a llamar al adaptador "adapter.setRecipes" y le enviamos lo mismo
                que recibimos,

                        @Override
                        public void setRecipes(List<Recipe> data) {
                            adapter.setRecipes(data);
                        }

            En "recipeUpdate"
                De forma similar se lo enviamos al, de hecho no hay
                que enviarle nada, porque lo va cambiar el repositorio, solo le avisamos al adaptador
                que cambiaron esos datos


                        @Override
                        public void recipeUpdated() {
                            adapter.notifyDataSetChanged();
                        }

            En "Delete"
                vamos a decir "adapter.RemoveRecipe" y le enviamos
                lo que estamos borrando, en el caso de "set" y "recipe" y "removeRecipe" dentro del adaptador,
                se está actualizando


                        @Override
                        public void recipeDeleted(Recipe recipe) {
                            adapter.removeRecipe(recipe);
                        }

Ahora, me hace falta, la implementación del "ClickListener" "onItemClickListener"
lo que nos implica, tener tres métodos más, "onFav" "onDelete" "onItem", "onFavClick"


            En onFav
                Voy a apoyarme en el presentador y voy a decir "toggleFavorite" sobre este "recipe"

                    @Override
                    public void onFavClick(Recipe recipe) {
                        presenter.toggleFavorite(recipe);
                    }


            En "onDelete"
                de forma similar voy a apoyarme sobre el presentador y "RemoveRecipe"

                    @Override
                    public void onItemClick(Recipe recipe) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceURL()));
                        startActivity(intent);
                    }

            En "onItemClick"
                voy a abrir el "URL" que tiene como "sourceUrlRecipe" entonces vamos hacer un
                "Intent intent = new  Intent(Intent.ACTION_VIEW, Url.parse.getSourceURL)" y luego iniciamos la actividad en base a este
                "intent" con esto tenemos lista nuestra vista, pero todavía no podemos probarlo, porque
                no tenemos un adaptador ni tampoco un presentador es decir, van a ver muchos "nulls" por aquí,
                nuestro siguiente paso entonces, va ser probar, si esta implementación que llevamos hasta
                el momento.
                    @Override
                    public void onDeleteClick(Recipe recipe) {
                        presenter.removeRecipe(recipe);
                    }


Commit21
            :Prueba_Adapter


Vamos a realizar la prueba de nuestro adaptador para ver si quedo bien con
datos, que no son los reales, como lo probamos,
        necesitamos un adaptador y
        un presentador

El adaptador necesita
        un listado,
        un ImageLoader,
        onItemClickListener,

Entonces yo tengo aquí, preparadas un par de cosas, vamos
a cargar un ImageLoader, esto solo para la prueba

Un solo Recipe, para la prueba
y en base a esto construimos el Adapter, "new RecipesAdapter" le envió
"arrays.asList" lo que le envió es únicamente este "recipe" luego el
"imagenLoader" y por último el "onItemClickListener" es "this" y el
presentador para no tener "null pointer exception" vamos a hace un "new recipe
list presenter" con todo implementado y lo vamos a ejecutar


    private void setupInjection() {
        ImageLoader loader= new GlideImageLoader(Glide.with(this));
        Recipe recipe=new Recipe();
        recipe.setFavorite(false);
        recipe.setTitle("Preuba");
        recipe.setImageURL("http://static.food2fork.com/icedcoffee5766.jpg");
        recipe.setSourceURL("http://static.food2fork.com/icedcoffee5766.jpg");
        adapter= new RecipesAdapter(Arrays.asList(recipe),loader,this);
        presenter = new RecipeListPresenter() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public void getRecipes() {

            }

            @Override
            public void removeRecipe(Recipe recipe) {

            }

            @Override
            public void toggleFavorite(Recipe recipe) {

            }

            @Override
            public void onEventMainThread(RecipeListEvent event) {

            }

            @Override
            public RecipeListView getView() {
                return null;
            }
        };
    }


Commit22 :
        MainListPresenterImpl_Event
Tramtamos de dar la lógica directa para poder usarla, de acuerdo a que la capa del interactuador sea el que use la parte de
recuperar datos ya sea desde la base como desde una api externa en donde puedo usarla.
Cremos la respectiva lógica de presentador para usar la implementación de nuestras clases siguiendo el siguinte esquema

-recipeMainList
    -recipeListpresenterImpl


En esta clase necesitaremos
    -eventBus
    -recipeListView
    -recipeListInteractor
    -StoredRecipesInteractor

EN los métodos

    en onCreate
        registramos el bus de eventos
            public void onCreate() {
                eventBus.register(this);
            }

    en onDestroy
        deregistrarme y volver nula la vista

        public void onDestroy() {
                eventBus.unregister(this);
                view=null;
            }
    en getRecipes
        me va permitir llamar al interactuador "execute"
        que es del RecipeListInteractor
            public void getRecipes() {
                recipeListInteractor.execute();
            }

    en "RemoveRecipe"
        me va permitir, usar el otro interactuador "storedInteractor" y vamos a dar un "executeDelete" enviando "recipe"

    public void removeRecipe(Recipe recipe) {
        storedRecipesInteractor.executeDelete(recipe);
    }

    en "toggleFavorite"
        Acá agregamos el cambio de boolean fav = recipe.getFavorite,  y recipe.setFavorite, dándole
        la negación de lo que teníamos inicialmente de tal forma que el presentador lo está cambiando y las demás capas se encargan de guardarlo eventualmente.
        de una forma similar "executeUpdate" enviándole el "recipe"
    public void toggleFavorite(Recipe recipe) {
        boolean fav=recipe.isFavorite();
        recipe.setFavorite(!fav);
        storedRecipesInteractor.executeUpdate(recipe);
    }

    en "getView"
        me va servir para las pruebas, voy a devolver la vista
    public RecipeListView getView() {
        return this.view;
    }


    en "onEventMainThread"
        que tiene que tener una suscripción para el manejo
        de eventos
        Verificamos si la vista es diferente de null y como tenenmos tres tipos de eventos pues podemos tomar un switch para este trámite
        dependiendo de los csasos por ejemplo

            switch (event.getType()){
                case RecipeListEvent.READ_EVENT:
                    view.setRecipes(event.getRecipeList());//enviamos la lista de eventos obtenidos
                    break;
                case RecipeListEvent.UPDATE_EVENT:
                    view.recipeUpdated();// solo actualizamos la vista del recipe que hemos realizado
                    break;
                case RecipeListEvent.DELETE_EVENT:
                    Recipe recipe= event.getRecipeList().get(0);//como estoy recibiendo un listado y solo necesito uno
                    view.recipeDeleted(recipe);// le envío esto y se acaba
                    break;
            }

Para los eventos
        En RecipeListEvent

            agregamos un tipo
            agregamos un listado de recipes
            agregamos los tipos de evento, Read,Update,Delete. como constantes

            agregamos los getters y setters
