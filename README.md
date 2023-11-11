# Unlocking the Power of Multi-tenancy (Spring Boot and Vaadin Flow)

![front_page](https://github.com/fredpena/barcamp2023/assets/5680906/cdb9a7d4-4ee9-4d81-ba15-35f00882460c)

### Descargo de responsabilidad

- La información proporcionada en esta presentación tiene como objetivo educativo y se basa en mi experiencia personal y
  conocimientos actuales. Si bien he hecho todo lo posible para garantizar la precisión y la actualidad de la
  información presentada, no puedo garantizar su exactitud completa.
- El uso de las tecnologías mencionadas, está sujeto a los términos y condiciones de cada herramienta. Es
  responsabilidad del usuario realizar su propia investigación y cumplir con las directrices y políticas de cada
  tecnología antes de implementarlas en su entorno de producción.
- Además, cabe señalar que las mejores prácticas y las soluciones presentadas en esta charla pueden variar según los
  requisitos y las circunstancias específicas de cada proyecto. Recomiendo encarecidamente realizar pruebas exhaustivas
  y consultar con profesionales capacitados antes de implementar cualquier solución en un entorno de producción.
- En resumen, mientras que esta presentación busca proporcionar información útil y práctica, el uso de las tecnologías y
  las decisiones de implementación son responsabilidad del usuario final. No asumo ninguna responsabilidad por los
  resultados derivados de la aplicación de los conceptos discutidos en esta presentación.

  ### Recurso
  [Presentación - Google Slide](https://docs.google.com/presentation/d/1bTXQc0ziBKF3JNxAsh3UVhRrp2zYXvAdOZqihMEx84M/edit?usp=sharing)

Este proyecto Multi-Tenant, nos embarcaremos en la creación de una simulación de una agenda de contactos. En este
escenario, la información sobre los contactos de diversas empresas se almacenará utilizando un enfoque de
Schema-per-tenant (Esquema por tenant).

En lo que respecta a la autenticación, contemplaremos usuarios que pueden estar registrados en uno o varios tenants,
utilizando las mismas credenciales. Sin embargo, se asignarán niveles de permisos distintos para cada tenant, asegurando
así una gestión independiente de los accesos y privilegios.

- **Multi-Tenant:** En un entorno Multi-Tenant, el software o sistema está diseñado para servir a múltiples clientes o "
  tenants". Cada
  tenant es una entidad independiente que utiliza los recursos del sistema, pero comparte la misma infraestructura
  subyacente. En tu caso, cada empresa que utiliza la simulación de la agenda de contactos se consideraría un "tenant".

- **Schema-per-Tenant:** Este enfoque implica tener un esquema de base de datos separado para cada tenant. Cada tenant
  tiene su propia estructura
  de base de datos para almacenar la información de los contactos. Esto garantiza la segregación de datos entre tenants,
  lo que es crucial para la privacidad y seguridad de la información.

- **Autenticación:** La autenticación permite a los usuarios acceder al sistema. En este proyecto, los usuarios pueden
  estar registrados en
  uno o varios tenants. Esto significa que un usuario puede tener acceso a la información de contactos de diferentes
  empresas utilizando las mismas credenciales.

- **Niveles de Permisos:** Aunque los usuarios pueden tener acceso a varios tenants, se asignan niveles de permisos
  distintos para cada uno. Esto
  asegura que, aunque un usuario tenga acceso a múltiples empresas, sus privilegios dentro de cada tenant sean
  gestionados
  de manera independiente. Por ejemplo, un usuario puede tener permisos de administrador en un tenant y solo permisos de
  lectura en otro.

- **Gestión Independiente de Accesos y Privilegios:** Al asignar niveles de permisos específicos para cada tenant, se
  logra una gestión independiente de accesos y
  privilegios. Esto significa que cada empresa puede controlar quién tiene acceso a su información de contactos y qué
  acciones pueden realizar. La gestión independiente es esencial para garantizar la seguridad y la privacidad de los
  datos
  de cada tenant.

- **Simulación de Agenda de Contactos:** La simulación de una agenda de contactos implica la creación, actualización y
  eliminación de información de contactos
  para cada empresa. Puede incluir campos como nombre, número de teléfono, dirección, etc. La simulación debería
  reflejar
  de manera realista el manejo de contactos en un entorno empresarial.

### Ejecutando la aplicación

El proyecto es un proyecto estándar de Maven. Para ejecutarlo desde la línea de comando,
escriba `make start` (Mac & Linux), luego abra http://localhost:60269 en su navegador.

El comando `make start`, es una forma eficiente de automatización del proceso de configuración y ejecución
de la aplicación en un entorno Docker. Esta abstracción simplifica enormemente, ya que
permite poner en marcha la aplicación sin tener que preocuparse por los detalles específicos de la configuración de
la aplicación y otros procesos de inicio.

#### Credenciales:

| **Usuario** | **Contraseña** |
|-------------|----------------|
| root        | 1234f          | 
| f.pena      | 1234f          | 
| m.perez     | 1234f          |

También puede importar el proyecto a su IDE de elección como lo haría con cualquier
Proyecto Maven. Leer más
en [cómo importar proyectos de Vaadin a diferentes IDE](https://vaadin.com/docs/latest/guide/step-by-step/importing) (
Eclipse, IntelliJ IDEA, NetBeans, y VS Code).

## Implementación en producción

Para crear una compilación de producción, llame a `mvnw clean package -Pproduction` (Windows),
o `./mvnw clean package -Pproduction` (Mac y Linux).
Esto creará un archivo JAR con todas las dependencias y recursos de front-end,
listo para ser implementado. El archivo se puede encontrar en la carpeta `target` una vez que se completa la
compilación.

Una vez creado el archivo JAR, puede ejecutarlo usando
`java -jar target/barcamp.jar`


## Implementación usando Docker

Para construir la versión Dockerizada del proyecto, ejecute

```
mvn clean package -Pproduction
docker build . -t barcamp:latest
```

Una vez que la imagen de Docker esté correctamente creada, puede probarla localmente usando

```
docker run -p 60269:60269 barcamp:latest
```
