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