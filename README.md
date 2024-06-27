# JournalApp

1)Agregar las dependencias y permisos en el Manifest y el gradle de la aplicacion

(Manifest)
<uses-permission android:name="android.permission.INTERNET" />
//Para obtener el singin certificate de nuestro proyecto android y agregarlo al momento de crear nuestro proyecto en firebaseFirestore, se puede correr este comando en la terminal 
./gradlew signingReport /// donde se debera seleccionar el SHA1 para agregarlo ahi donde lo solicita la configuracion del firebase



(GRADLE)
//Esto se debe colocar en el build gradle de la aplicacion
id("com.google.gms.google-services") version "4.4.2" apply false

//Esto se debe colocar en el build gradle del Module:App 
id("com.google.gms.google-services") 
//Firestore
implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
implementation("com.google.firebase:firebase-analytics")

// Import the BoM for the Firebase platform
implementation(platform("com.google.firebase:firebase-bom:32.7.1"))

// Declare the dependency for the Cloud Firestore library
// When using the BoM, you don't specify versions in Firebase library dependencies
implementation("com.google.firebase:firebase-firestore")
//Auth
implementation("com.google.firebase:firebase-auth")
//storage
implementation("com.google.firebase:firebase-storage")
//glide
implementation("com.github.bumptech.glide:glide:4.16.0")
annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

2)Agregar el archivo google-services.json en la raiz del proyecto, esto no los proporciona la pagina de Firebase una vez creemeos un proyecto ahi

3)Documentacion 
https://firebase.google.com/docs/build?authuser=0&hl=es-419
Storage 
https://console.firebase.google.com/u/0/project/journalapp-522ee/storage/journalapp-522ee.appspot.com/files
DATABASE
https://console.firebase.google.com/u/0/project/journalapp-522ee/firestore/databases/-default-/data/~2FJournal~2FAzpEOn3wsRAttDfHbfZN
//AUTH
https://console.firebase.google.com/u/0/project/journalapp-522ee/authentication/users

