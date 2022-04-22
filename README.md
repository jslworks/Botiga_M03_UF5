# JavaFX Shop - IES Badia - 2n DAW

## Contenidos
- [Introduccion](#Introduccion)
- [Instalacion y configuracion](#Instalacion-y-configuracion)
- [Estructura](#Estructura)
- [Desarrollo](#Desarrollo)
- [Webgrafia](#Webgrafia)

## Introduccion
Este es un proyecto educativo, como alumno del centro IES Badia, y tiene como objetivo para aprender a utilizar la librería JavaFX. Se considera finalizado
El aplicativo consiste en una tienda dónde se pueden gestionar Personas y Productos o Packs. Al empezar, se accede a un menú para entrar en una de ellas. En sus pantallas se muestra un formulario para poder crear ya sean Personas o Productos. Más tarde se podría gestionar.

<img src="./img/intro/1-main.png" alt="main">
<img src="./img/intro/2-persona.png" alt="persona">
<img src="./img/intro/3-producto-pack.png" alt="Producto_pack">

## Instalacion y configuracion
### Eclipse y JDK
Para desarrollar vamos a utilizar el entorno de eclipse, configuraremos las dependencias y el modo de ejecución adecuado para correr JavaFX. 
1. Instalamos eclipse a través de su página oficial ( https://www.eclipse.org/downloads/ )
2.Luego descargamos el SDK que se ejecutará desde eclipse.
<img src="./img/install/1.sdk.png" alt="sdk">
3. Una vez hecho, entramos en Eclipse y vamoss al Market para instalar la extensión de FX
<img src="./img/install/2.extensionFX.png" alt="extensionFX">
4. Descargamos las librerías necesarias para ejecutar JavaFX
<img src="./img/install/3.obtenerLibreriasFX.png" alt="cómo obtener librerias FX">
5. Crear librería de usuario y asignar librerías FX para el proyecto
<img src="./img/install/4.asignarLibreriasFX.png" alt="asignar librerias FX">
6. Ahora configuraremos el entorno de trabajo del proyecto
-	Arranque: asignamos un nombre para la configuración, establecemos qué proyecto es y cual es el objeto inicial por dónde comenzará la ejecución 
<img src="./img/install/5.arranque.png" alt="arranque">
-	Argumentos: al iniciarse el proyecto tendrá en cuenta éstas dependencias gráficas para poder arrancar
<img src="./img/install/6.argumentos.png" alt="argumentos">
-	Comprobamos que el entorno de ejecución sea el JDK adecuado
<img src="./img/install/7.enviroment.png" alt="entorno de trabajo">
-	Por último, comprobar que las dependencias sean correcta y le decimos al programa que añada todos los módulos que estén en el proyecto (ALL-MODULE-PATH)
<img src="./img/install/8.dependencias.png" alt="dependencias">
 
## Estructura
La distribución de proyecto está basada en el modelo MVC y sería la siguiente:
<ul>
<li>src: contiene el código fuente</li>
<ul>
<li>modelo</li>
<ul>
<li>bo</li>
<li>dao</li>
</ul>
<li>vista</li>
<li>controlador</li>
</ul>
<li>lib: almacena las dependencias</li>
<li>bin: guarda ficheros para ejecutar</li>

Dentro del src, en las vistas encontraremos también los ficheros de idiomas.
El modelo está formado por un gestor DAO (objetos de acceso de datos) y las clases que guardan datos, los BO (objetos de negocio).

<img src="./img/estructura/estructura.png" alt="estructura de proyecto">