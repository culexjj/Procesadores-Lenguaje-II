# Procesadores-Lenguaje-II

**Contenido:** Escáner, parser y clase traducción código intermedio a código final

**Contacto:** culex.jj@gmail.com

**Estado:** funcional (solo parte obligatoria de la practica)


## El analizador semántico y la comprobación de tipos.


Durante la etapa de análisis semántico se define el significado de cada construcción del lenguaje de forma que el compilador pueda interpretar de forma única y precisa todas y cada una de las sentencias del lenguaje fuente. Para ello se usa como base, la representación intermedia en forma de árbol generada durante el análisis sintáctico.

El funcionamiento del analizador semántico, objeto de evaluación, es consistente con lenguaje cES v1.0 y reconoce todas sus construcciones.

Son responsabilidad del analizador semántico las siguientes tareas:

• Gestión de la pila de ambientes; Principal, subprograma, bloques anónimos.

• Gestión de la tabla de símbolos (TS); Del ámbito correspondiente, para cada uno de los identificadores y atributos asociados al mismo. No se permite la declaración de dos símbolos con el mismo nombre en el mismo ámbito.

• Gestión de la tabla de tipos (TT). Del ámbito correspondiente, para cada uno de los tipos y atributos asociados al mismo. No se permite la declaración de dos tipos con el mismo nombre, aunque pertenezcan a distintos ámbitos, si desde un ámbito se puede alcanzar el otro ámbito

• Control de tipos; Cada símbolo debe existir en al ámbito correspondiente y los tipos asociados deben de ser consistentes con la expresión que se evalúa

• Control corrección semántica,


Para ello se han realizado las siguientes acciones:


• Factorizar la gramática: Como paso previo se ha factorizado la gramática para evitar conflictos durante el análisis sintáctico.

• Modificar relaciones de precedencia.

• Añadir de acciones semánticas responsables de la lógica de traducción.

• Creación clases para el manejo de Tokens no terminales. (e.g.: Id, ListadoIDs, Sentencia, ListadoSentencias)

• Modificación clases arquitectura proporcionada por el ED, solo en caso necesario. (e.g.: TypeSimple – constructor tipos primitivos, TypeArray – atributos tipoVector y tamaño, SymbolConstant – atributo valor, SymbolFunction – atributos tipoRetorno y listaParametros)

• Propagación de la información semántica (atributos).

• Subprogramas: control parámetros; número y orden de estos, tipo de retorno en funciones y sentencia devuelve.

• Control de rangos; en estructuras de datos tipo vector.

• Resto de controles semánticos.

• Gestion de errores léxicos, sintácticos, semánticos ( LexicalErrorManager, SyntaxErrorManager, SemanticErrorManager).


## Generación de código intermedio

El código intermedio es una secuencia de instrucciones cercanas al código destino, pero independientes de cualquier arquitectura física.
Para la generación del código intermedio se ha usado la representación lineal, realizada por Javier Vélez Reyes, donde cada elemento se compone de: código de operación, hasta dos operandos de entrada y uno de salida.

## Generación de código final

En esta fase se traduce el código intermedio al código final adecuado para la arquitectura hardware destino, en este caso el simulador ENS2001. El código objeto resultante del proceso de compilación es un subconjunto del estándar Ensamblador IEEE 694.


