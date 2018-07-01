                               App Cliente
Ventana de autenticación: Pide login y password
Si el cliente recibe el UUID del coordinador, entonces se pasa a la ventana de usuarios. Si
no se queda en la ventana de autenticación.
Ventana de usuarios
Presenta una tabla de todos los usuarios registrados en el sistema con la siguiente
               
               información:
                    1. Nombre Completo
                    2. LoginName
                    3. Fecha de creación
                    4. Fecha de accesos al sistema
 
-                                
                                App Coordinador
Recibe los requests de autenticación de los clientes y lo redirecciona a un servidor de
autenticación para obtener el respectivo UUID si este lo tiene. La elección del servidor lo
pueda hacer roundrobin.
                                App Servidor autenticación
Procesa los requests del coordinador y envía la respuesta correspondiente. Guarda la
información de los usuarios en una base de datos.

                                Interfaz del cliente
1. loginRequest(loginName, password): retorna UUID si tiene asignado el loginName,
sino retorna error.
2. createUser(nombreCompleto, loginName, password): no retorna
Asigna al usuario su respective UUID
3. lookupUser(UUID): retorna data del usuario (excepto password)
4. lookupUser(loginName): retorna data del usuario (excepto password)
5. deleteUser(loginName, password): retorna T o F
6. updateLoginUser(newloginName, oldloginName)
7. getUsers():retorna los datos de todos los usuarios
