package compiler.code;

import java.util.Arrays;
import java.util.List;

import compiler.intermediate.Label;
import compiler.intermediate.Temporal;
import compiler.intermediate.Value;
import compiler.intermediate.Variable;
import compiler.semantic.type.TypeSimple;

import es.uned.lsi.compiler.code.ExecutionEnvironmentIF;
import es.uned.lsi.compiler.code.MemoryDescriptorIF;
import es.uned.lsi.compiler.code.RegisterDescriptorIF;
import es.uned.lsi.compiler.intermediate.LabelFactory;
import es.uned.lsi.compiler.intermediate.LabelFactoryIF;
import es.uned.lsi.compiler.intermediate.LabelIF;
import es.uned.lsi.compiler.intermediate.OperandIF;
import es.uned.lsi.compiler.intermediate.QuadrupleIF;

/**
 * Class for the ENS2001 Execution environment.
 */

public class ExecutionEnvironmentEns2001 
    implements ExecutionEnvironmentIF
{    
    private final static int      MAX_ADDRESS = 65535; 
    private final static String[] REGISTERS   = {
       ".PC", ".SP", ".SR", ".IX", ".IY", ".A", 
       ".R0", ".R1", ".R2", ".R3", ".R4", 
       ".R5", ".R6", ".R7", ".R8", ".R9"
    };
    
    private RegisterDescriptorIF registerDescriptor;
    private MemoryDescriptorIF   memoryDescriptor;
    
    /**
     * Constructor for ENS2001Environment.
     */
    public ExecutionEnvironmentEns2001 ()
    {       
        super ();
    }
    
    /**
     * Returns the size of the type within the architecture.
     * @return the size of the type within the architecture.
     */
    @Override
    public final int getTypeSize (TypeSimple type)
    {      
        return 1;  
    }
    
    /**
     * Returns the registers.
     * @return the registers.
     */
    @Override
    public final List<String> getRegisters ()
    {
        return Arrays.asList (REGISTERS);
    }
    
    /**
     * Returns the memory size.
     * @return the memory size.
     */
    @Override
    public final int getMemorySize ()
    {
        return MAX_ADDRESS;
    }
           
    /**
     * Returns the registerDescriptor.
     * @return Returns the registerDescriptor.
     */
    @Override
    public final RegisterDescriptorIF getRegisterDescriptor ()
    {
        return registerDescriptor;
    }

    /**
     * Returns the memoryDescriptor.
     * @return Returns the memoryDescriptor.
     */
    @Override
    public final MemoryDescriptorIF getMemoryDescriptor ()
    {
        return memoryDescriptor;
    }

    /**
     * Translate a quadruple into a set of final code instructions. 
     * @param cuadruple The quadruple to be translated.
     * @return a quadruple into a set of final code instructions. 
     */
    @Override
    public final String translate (QuadrupleIF quadruple)
    {      
       
    	//Inicializar VARGLOBAL (Copiar #dato a posicion de /memoria) Quadruple - [VARGLOBAL x, 0, null]
    	if(quadruple.getOperation().equals("VARGLOBAL")) {
    	StringBuffer b = new StringBuffer();
    	String o1 = operacion(quadruple.getFirstOperand()); //DATO 0
    	String resultado = operacion(quadruple.getResult()); //POSICION MEMORIA X
    	//b.append(";" + quadruple.toString() + "\n"); 
    	b.append("MOVE " + o1 + ", " + resultado); //copiar dato en posicion memoria
    	return b.toString();
    	}
    	
    	
    	//Inicializar VARGLOBALVECTOR (Copiar #dato a posicion de /memoria) [VARGLOBALVECTOR vector, 65533, 0]
    	if(quadruple.getOperation().equals("VARGLOBALVECTOR")) {
    	StringBuffer b = new StringBuffer();
    	String o2 = operacion(quadruple.getSecondOperand());//DATO 0
    		OperandIF o1 = quadruple.getFirstOperand(); //POSICION MEMORIA 65533
    	//b.append(";" + quadruple.toString() + "\n"); 
    		b.append("MOVE " + o2 + ", " + "/" + o1); //copiar dato en posicion memoria
    	return b.toString();
    	}
    	
    	
    	//Inicializar MV (Copiar dato #Y a posicion de memoria /X) Quadruple - [MV X, Y, null]
    	if(quadruple.getOperation().equals("MV")) {
    	StringBuffer b = new StringBuffer();
    	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y
    	String resultado = operacion(quadruple.getResult()); //POSICION MEMORIA X
    	////b.append(";" + quadruple.toString() + "\n"); 
    	b.append("MOVE " + o1 + ", " + resultado); //copiar dato en posicion memoria
    	return b.toString();
    	}
    	
    	
    	//Inicializar MVA  Quadruple - [MVA X, Y, null]
    	//MVA recupera la dirección de memoria de Y, y lo guarda en el X (RECUPERA DIRECCION MEMORIA)
    	//se usa en la parte de referencia
    	if(quadruple.getOperation().equals("MVA")) {
    	StringBuffer b = new StringBuffer();
    	String o1 = operacion(quadruple.getFirstOperand()); //POSICION MEMORIA Y
    	String resultado = operacion(quadruple.getResult()); //POSICION MEMORIA X
    	//b.append(";" + quadruple.toString() + "\n"); 
    		b.append("MOVE " + "#" + o1.substring(1) + ", " + resultado); //copiar posicion de memoria en posicion memoria
    	return b.toString();
    	}
    	
    	
    	//Inicializar STP  Quadruple - [STP X, Y, null]
    	//STP recupera el valor de Y, lo guarda en la direccion de memoria apuntada por X (MUEVE DATO)
    	if(quadruple.getOperation().equals("STP")) {
    	StringBuffer b = new StringBuffer();
    	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y
    	String resultado = operacion(quadruple.getResult()); //POSICION MEMORIA X
    	//b.append(";" + quadruple.toString() + "\n");
    	b.append("MOVE " + resultado + ", " + ".R1" + "\n"); //direccion de memoria  apuntado por X --> R1
    	b.append("MOVE " + o1 + ", " + "[.R1]"); //Muevo el valor de Y a la direccion guardada en R1 n (que es la direccion de X)
    	return b.toString();
    	}
    	
    	
    	//Inicializar MVP Quadruple - [MVP X, Y, null]
    	//MVP recupera el contenido de Y, y lo guarda en X (MUEVE DATO)
    	//se usa en la parte de expresion
    	if(quadruple.getOperation().equals("MVP")) {
    	StringBuffer b = new StringBuffer();
    	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y
    	String resultado = operacion(quadruple.getResult());//POSICION MEMORIA X  	
    	//b.append(";" + quadruple.toString() + "\n"); 
    	if(quadruple.getFirstOperand() instanceof Variable) { //Variable
    		b.append("MOVE " + o1 + ", " + resultado); //Muevo el valor de Y a la direccion de X
    	} else { //Vector
    		b.append("MOVE " + o1 + ", " + ".R1" + "\n");
    		b.append("MOVE " + "[.R1]" + ", " + resultado);
    	}
    	return b.toString();
    	}
    	
    	
    	//Inicializar EQ Quadruple - [EQ X, Y, Z]
    	if(quadruple.getOperation().equals("EQ")) {    		
    		LabelFactoryIF lf = new LabelFactory();
			LabelIF etiqueta1 = lf.create();
			LabelIF etiqueta2 = lf.create();
			
        	StringBuffer b = new StringBuffer();
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z
        	String resultado = operacion(quadruple.getResult());//POSICION MEMORIA X  	  
        	
        	//b.append(";" + quadruple.toString() + "\n"); 
        	b.append("CMP " + o1 + ", " + o2 + "\n");
        	b.append("BZ /" + etiqueta1 + "\n");
        	b.append("MOVE #0," + resultado + "\n");
        	b.append("BR /" + etiqueta2 + "\n");
        	b.append(etiqueta1 + ": MOVE #1," + resultado + "\n");
			b.append(etiqueta2 + ": NOP");      	
        	return b.toString();
        	}
    	
    	
    	
    	//Inicializar LS Quadruple - [LS X, Y, Z]
    	if(quadruple.getOperation().equals("LS")) {
    		LabelFactoryIF lf = new LabelFactory();
			LabelIF etiqueta1 = lf.create();
			LabelIF etiqueta2 = lf.create();
			
        	StringBuffer b = new StringBuffer();
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z  
        	String resultado = operacion(quadruple.getResult());//POSICION MEMORIA X  	
        	//b.append(";" + quadruple.toString() + "\n"); 
	        	//ajustamos el valor del segundo operando
				b.append("MOVE " + "#1" + ", " + ".R1" + "\n");
				b.append("SUB " + o2 + ", " + ".R1" + "\n");
	           	b.append("MOVE .A, " + o2 + "\n");
        	b.append("SUB " + o1 + ", " + o2 + "\n");
        	b.append("BZ /" + etiqueta1 + "\n");
        	b.append("BN /" + etiqueta1 + "\n");
        	b.append("MOVE #0," + resultado + "\n");
        	b.append("BR /" + etiqueta2 + "\n");
        	b.append(etiqueta1 +  ": NOP" + "\n");
        	b.append("MOVE #1," + resultado + "\n");
			b.append(etiqueta2 + ": NOP");  
        	
        	return b.toString();
        	}
    	    	
    	
    	//Inicializar BRF Quadruple - [BRF X, Y, null] 
    	if(quadruple.getOperation().equals("BRF")) {
        	StringBuffer b = new StringBuffer();
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y (NOMBRE ETIQUETA) 	
        	String resultado = operacion(quadruple.getResult());//POSICION MEMORIA X  	
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("CMP #1, " + resultado + "\n");
        	b.append("BNZ " + "/" + o1);
        	return b.toString();
        	}
    	
    	
    	//Inicializar BRT Quadruple - [BRT X, Y, null] 
    	if(quadruple.getOperation().equals("BRT")) {
        	StringBuffer b = new StringBuffer();
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y (NOMBRE ETIQUETA) 	
        	String resultado = operacion(quadruple.getResult());//POSICION MEMORIA X  	
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("CMP #0, " + resultado + "\n");
        	b.append("BNZ " + "/" + o1);
        	return b.toString();
        	}
    	
    	
    	//Inicializar INL Quadruple - [INL X, null, null]
    	//Insertar etiqueta X (Etiqueta) 
    	if(quadruple.getOperation().equals("INL")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //DATO X  (NOMBRE ETIQUETA)   	
        	//b.append(";" + quadruple.toString() + "\n"); 
        	b.append(resultado + ": " +  "NOP");
        	return b.toString();
        	}
    	
    	
    	//Inicializar BR Quadruple - [BR X, null, null]
    	//Salto a X (Etiqueta)
    	if(quadruple.getOperation().equals("BR")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //DATO X  (NOMBRE ETIQUETA)  	
        	//b.append(";" + quadruple.toString() + "\n"); 
        	b.append("BR " + "/" + resultado); 
        	return b.toString();
        	}
    	
    	
    	//Inicializar WRITEINT Quadruple - [WRITEINT T_19, null, null]
    	if(quadruple.getOperation().equals("WRITEINT")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //DATO X
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("WRINT " +  resultado + "\n"); 
        	b.append("WRCHAR #13"); 
        	return b.toString();
        	}
    	
    	
    	//Inicializar WRITESTRING Quadruple - [WRITESTRING X, Y, null]
    	if(quadruple.getOperation().equals("WRITESTRING")) {
        	StringBuffer b = new StringBuffer();
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y (NOMBRE ETIQUETA)
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("WRSTR " + "/" + o1 + "\n"); 
        	b.append("WRCHAR #13 "); 
        	return b.toString();
        	}
    	
    	
    	//Inicializar CADENA Quadruple - [CADENA "z es mayor que x", L_0, null]
    	if(quadruple.getOperation().equals("CADENA")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //DATO X  (cadena de caracteres)        	
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y (NOMBRE ETIQUETA)
        	//b.append(";" + quadruple.toString() + "\n");
           	b.append(o1 + ": DATA " + resultado);
        	return b.toString();
        	
        	}
    	
    	//Inicializar SUB Quadruple - [SUB X, Y, Z]
    	if(quadruple.getOperation().equals("SUB")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO Y - Z
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z 
        	//b.append(";" + quadruple.toString() + "\n");
           	b.append("SUB " + o1 + ", " + o2 + "\n");
           	b.append("MOVE .A, " + resultado);
        	return b.toString();
        	}
    	
    	//Inicializar ADD Quadruple - [ADD X, Y, Z]
    	if(quadruple.getOperation().equals("ADD")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO Y + Z
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z 
        	//b.append(";" + quadruple.toString() + "\n");
           	b.append("ADD " + o1 + ", " + o2 + "\n");
           	b.append("MOVE .A, " + resultado);
        	return b.toString();
        	}
    	
    	// Inicializar MUL Quadruple - [MUL X, Y, Z]
    	if(quadruple.getOperation().equals("MUL")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO Y * Z
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z 
        	//b.append(";" + quadruple.toString() + "\n");
           	b.append("MUL " + o1 + ", " + o2 + "\n");
           	b.append("MOVE .A, " + resultado);
        	return b.toString();
        	}
    	
    	//Inicializar INC Quadruple - [INC X, Y, null]
    	if(quadruple.getOperation().equals("INC")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO Y + Z
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	//b.append(";" + quadruple.toString() + "\n");
           	b.append("MOVE " + "#1" + ", " + ".R1" + "\n");
        	b.append("ADD " + o1 + ", " + ".R1" +  "\n");
           	b.append("MOVE .A, " + resultado);
        	return b.toString();
        	}
    	
    	//Inicializar AND Quadruple - [AND X, Y, Z] 
    	if(quadruple.getOperation().equals("AND")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO X
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	String o2 = operacion(quadruple.getSecondOperand()); //DATO Z 
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("AND " + o1 + ", " + o2 +  "\n");
           	b.append("MOVE .A, " + resultado);
        	return b.toString();
        	}
    	
    	
    	//Inicializar NOT Quadruple - [NOT X, Y, null] 
    	if(quadruple.getOperation().equals("NOT")) {
        	StringBuffer b = new StringBuffer();
        	String resultado = operacion(quadruple.getResult()); //RESULTADO X
        	String o1 = operacion(quadruple.getFirstOperand()); //DATO Y 
        	//b.append(";" + quadruple.toString() + "\n");
			b.append("NOT " + o1);
        	return b.toString();
        	}
    	
    	//Inicializar HALT Quadruple - [HALT null, null, null]
    	if(quadruple.getOperation().equals("HALT")) {
        	StringBuffer b = new StringBuffer();
        	//b.append(";" + quadruple.toString() + "\n");
        	b.append("HALT ");
        	return b.toString();
        	}    	
    	
        return quadruple.toString(); 
    }
    
    
    
    
    
    private String operacion (OperandIF o) {
    	
    	if(o instanceof Variable) {
    	return "/" + ((Variable)o).getAddress();
    	}
    	
    	if(o instanceof Value){
    	return "#" + ((Value)o).getValue();
    	}
    	
    	if(o instanceof Temporal){
    	return "/" + ((Temporal)o).getAddress();
    	}
    	
    	if(o instanceof Label){
    	return ((Label)o).getName();
    	}
    	
    	return null;
    }
    
}

