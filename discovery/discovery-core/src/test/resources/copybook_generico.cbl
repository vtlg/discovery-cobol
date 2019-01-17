D   F973331  30OCT17 16:18                                              
                                                                        
      ******************************************************************
      * DCLGEN TABLE(MPDT024)                                          *
      *        LIBRARY(COD.PCS.V00.SAT.COPYS(MPDT024))                 *
      *        LANGUAGE(COBOL)                                         *
      *        QUOTE                                                   *
      * ... IS THE DCLGEN COMMAND THAT MADE THE FOLLOWING STATEMENTS   *
      ******************************************************************
           EXEC SQL DECLARE MPDT024 TABLE                               
           ( CODENT                         CHAR(4) NOT NULL,           
             CODMAR                         DECIMAL(2, 0) NOT NULL    
           ) END-EXEC.                                                  
      ******************************************************************
      * COBOL DECLARATION FOR TABLE MPDT024                            *
      ******************************************************************
       01  DCLMPDT024.                                                  
           10 CODENT               PIC X(4).                            
           10 CODMAR               PIC S9(2)V USAGE COMP-3.             
      ******************************************************************
      * THE NUMBER OF COLUMNS DESCRIBED BY THIS DECLARATION IS 9       *
      ******************************************************************
      ***************************************************************** 
      ** Comentário                                                  ** 
      ***************************************************************** 
      ** Comentário 2                                                ** 
      ***************************************************************** 
      ***************************************************************** 
        COPY AMCABECE.                                                  
        COPY ATBENEFI.        
		
       01 PARAM-CSC.                                                    
           03  AREA-ENTRADA-AECMCSC.                                    
               05  ENTORNO-AECMCSC              PIC X.                  
               05  CLAVE-GE-AECMCSC.                                    
                   07 IDENTIFIC-AECMCSC         PIC X(05).              
               05  DATOS-DE-SCRIPT.                                     
                       09  EL-COMANDO OCCURS 8,                         
                           INDEXED BY IND-COMANDO.                      
                           12 ETIQUETA-COMANDO  PIC X(01).              
               05 RETORNO.                                                
                   07  RETORNO-CRIP-CSC             PIC 9(08) COMP.         
                   10  D3-NSECFIC                   PIC S9(10)V.                
                   10  LIMCT-PORPAGOA               PIC 9(03)V9(04).    
                   10  SW-CHECK-LUHN-VALIDO         PIC    9(01).   
                      88  CHECK-LUHN-VALIDO    VALUE 1.                    
           03  (AMMIGINV).                                              
               10 (AMMIGINV)-CODENT     PIC X(04).                      
               10 DTCCAL-R-DT-CRIAC-ARQ  REDEFINES DTCCAL-DT-CRIAC-ARQ.  
         01  :T:-MESSAGE-STATUS EXTERNAL       PIC S9(4) BINARY.          
       01  :T:-DE-MAP EXTERNAL.                                         
           05  :T:-DE-MAP-ENTRY OCCURS 128 TIMES                        
                         INDEXED BY :T:-D.                              
               10  :T:-DE-FIRST-SUBFLD    INDEX.    
       01  :T:-DE-NAMES REDEFINES :T:-DE-MAP.                           
       01  TAZGHSM-OFFSET-P.                                       
           15 FILLER                          PIC  X VALUE X'00'.  