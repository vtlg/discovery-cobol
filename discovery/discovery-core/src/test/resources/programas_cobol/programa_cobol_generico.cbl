                                                                        
       IDENTIFICATION DIVISION.                                         
       PROGRAM-ID.      NOMEINTERNO.                                       
       AUTHOR           PICONE.                                         
       DATE-WRITTEN.    DEZEMBRO 2010.                                  
       ENVIRONMENT DIVISION.                                            
      *OBJETIVO:                                                        
      *   EXCLUIR A TABELA DE PRODUTOS / PARCEIROS.                     
      *                                                                 
      * PRODUTOS BANCARIOS.                                             
      *                                                                 
      *---------------------------------------------------------------- 
LADLA * 26/05/13 | LEANDRO   | INCLUIR GRAVACAO DE LOG AUDITORIA        
      *---------------------------------------------------------------- 
      *                                                                 
       CONFIGURATION                    SECTION.                        
       SPECIAL-NAMES.                                                   
           DECIMAL-POINT IS COMMA.                                      
		   
       INPUT-OUTPUT SECTION.                                            
                                                                        
       FILE-CONTROL.                                                    
                                                                        
           SELECT  ENTRADA1   ASSIGN   TO    E1DQ9730.                  
           SELECT  ENTRADA2   ASSIGN   TO    E2DQ9730.                  
           SELECT  SALIDA1    ASSIGN   TO    S1DQ9730.                  
                                                                        
      *+---------------------------------------------------------------+
      *                      DATA   DIVISION                            
      *                      ===============                            
      *+---------------------------------------------------------------+
       DATA DIVISION.                                                   
	   
       FILE SECTION.                                                    
                                                                        
       FD  ENTRADA1                                                     
           RECORDING MODE IS F                                          
           BLOCK CONTAINS 0 RECORDS                                     
           RECORD CONTAINS 500   CHARACTERS.                            
       01  R-ENTRADA1     PIC X(500).                                   
                                                                        
       FD  ENTRADA2                                                     
           RECORDING MODE IS F                                          
           BLOCK CONTAINS 0 RECORDS               
           DATA RECORD IS RG-FENTRADA1                                  		   
           RECORD CONTAINS 160   CHARACTERS.                            
       01  RG-FENTRADA1                   PIC X(401).                    
                                                                        
       FD  SALIDA1                                                      
           RECORDING MODE IS F                                          
           BLOCK CONTAINS 0 RECORDS                                     
           RECORD CONTAINS 395   CHARACTERS.                            
       01  R-SALIDA1      PIC X(395).             	   
	   
       WORKING-STORAGE                  SECTION.                        
                                                                       
      ***  CONSTANTES                                                   
       01  CT-LITERALES.                                                
           05  CT-ATR980                   PIC X(06)  VALUE 'ATR980'.
           05  CT-PCSPSD49                 PIC X(06)  VALUE 'PCSPSD49'.   
           05  CT-ATRG005                  PIC X(06)  VALUE 'ATRG005'.   
                                                                        
      ***  COMUNICACAO DO COM O DB2.                                    
           EXEC SQL  INCLUDE  SQLCA    END-EXEC.                        
                                                                        
      ***  TABELA DE PRODUTOS PARCEIROS.                                
           EXEC SQL  INCLUDE  PCSTBH02 END-EXEC.                        

      ***  TABELA DE PRODUTOS PARCEIROS.                                
           EXEC SQL  INCLUDE  MPDT083 END-EXEC.  
                                                                        
      ***  TABELA DE CODIGOS DO DB2.                                    
       01  TBSQLERR.                                                    
           COPY ATSQLERR.                                               
                                                                        
      ***  TELAS.                                                       
       01  REG-COMPLEMENTAR.                                            
           COPY PCSDS182.                                               
                                                                        
      *---- BOOKS SUB-ROTINA PCSPSD49                                   
LADLA  COPY PCSDSD49 REPLACING  ==:PCSDSD49:== BY ==LOG==.              

       LINKAGE                          SECTION.                        
       01  DFHCOMMAREA.                                                 
           COPY PCSDS001.                                               
                                                                        
      ***  PROCEDURE.                                                   
      *                                                                 
       PROCEDURE DIVISION USING DFHCOMMAREA.                            
       MODULO-INICIAL                   SECTION.                        
           PERFORM 100-00-INICIO.                                       
           PERFORM 110-00-PROCESS0.                                     
           PERFORM 900-00-FIM.                                          

       0005-UPDATE-TABELA.
      *------------------.                                              
          
           EXEC SQL                                                     
                UPDATE MPDT083                                          
                       SET NUMSEC      = :DCLMPDT083.NUMSEC,            
                           INDICA      = :DCLMPDT083.INDICA,            
                           PROGRAMA    = :DCLMPDT083.PROGRAMA,          
                           CADENA      = :DCLMPDT083.CADENA,            
                           DATOS       = :DCLMPDT083.DATOS              
                       WHERE (NUMSEC   = :DCLMPDT083.NUMSEC)   AND      
                             (INDICA   = :DCLMPDT083.INDICA)   AND      
                             (PROGRAMA = :DCLMPDT083.PROGRAMA) AND      
                             (CADENA   = :DCLMPDT083.CADENA)            
           END-EXEC.                        
							  
      ***  LER O PARCEIRO/PRODUTO.                                      
       0004-SELECT-TABELA                     SECTION.                        
           EXEC SQL                                                     
                 SELECT    CO_ENTIDADE        ,                         
                           CO_EMPSA_PARCEIRA  ,                         
                           CO_PRDTO_EMPRESA   ,                         
                           NO_PRDTO_PARCEIRA  ,                         
                           IC_SITUACAO        ,                         
                           CO_CODENTUMO       ,                         
                           CO_CODOFIUMO       ,                         
                           CO_USUARIOUMO      ,                         
                           CO_CODTERMUMO      ,                         
                           TS_CONTCUR                                   
                INTO                                                    
                    :PCSTBH02.CO-ENTIDADE        ,                      
                    :PCSTBH02.CO-EMPSA-PARCEIRA  ,                      
                    :PCSTBH02.CO-PRDTO-EMPRESA   ,                      
                    :PCSTBH02.NO-PRDTO-PARCEIRA  ,                      
                    :PCSTBH02.IC-SITUACAO        ,                      
                    :PCSTBH02.CO-CODENTUMO       ,                      
                    :PCSTBH02.CO-CODOFIUMO       ,                      
                    :PCSTBH02.CO-USUARIOUMO      ,                      
                    :PCSTBH02.CO-CODTERMUMO      ,                      
                    :PCSTBH02.TS-CONTCUR                                
                                                                        
                FROM PCSTBH02_PRDO_PRCA                                 
                WHERE CO_ENTIDADE        =  :CT-0104                    
                  AND CO_EMPSA_PARCEIRA  =  :WS-PARCEI-PAG              
                  AND CO_PRDTO_EMPRESA   =  :WS-PRODU-PAG               
           END-EXEC.
           EXEC SQL                                                     
             SELECT CURRENT TIMESTAMP INTO :WS-FECHA-CURR               
             FROM SYSIBM.SYSDUMMY1                                      
           END-EXEC.            

       0003-DELETE_TABELA           SECTION.                        
                                                                        
           EXEC SQL                                                     
                DELETE FROM PCSTBH02_PRDO_PRCA                          
                WHERE CO_ENTIDADE        =  :CT-0104                    
                  AND CO_EMPSA_PARCEIRA  =  :WS-PARCEI-PAG              
                  AND CO_PRDTO_EMPRESA   =  :WS-PRODU-PAG               
           END-EXEC.                                                    

       0005-EXEC-CICS.                                          
      *-----------------------.                                         
           EXEC CICS SYNCPOINT END-EXEC                                 
           EXEC CICS ASKTIME                                            
                     ABSTIME(WS-ABSTIME)                                
           END-EXEC                                                     
           EXEC CICS FORMATTIME                                         
                     ABSTIME(WS-ABSTIME)                                
                     TIME(WS-HORA-EDI)                                  
           END-EXEC                    
           EXEC CICS                                                    
                SYNCPOINT ROLLBACK                                      
           END-EXEC.
           EXEC CICS RETRIEVE INTO (MQM-TRIGGER-MESSAGE)NOHANDLE        
           END-EXEC.                                                    
           EXEC CICS RETURN END-EXEC                                 
           EXEC CICS START                                          
                       TRANSID (CT-PCX5)                                
                          FROM (WS-MENSAJE)                             
                        LENGTH (LENGTH OF WS-MENSAJE)                   
           END-EXEC. 
           EXEC CICS RETRIEVE                                           
                         INTO (WS-MENSAJE)                              
                       LENGTH (LENGTH OF WS-MENSAJE)                    
           END-EXEC                           
VIC        EXEC CICS READQ TS                                          
VIC                 QNAME    ( WS-TS-ALTERACAO          )              
VIC                 INTO     ( WS-CONTEUDO-TS           )              
VIC                 LENGTH   ( LENGTH OF WS-CONTEUDO-TS )              
VIC                 ITEM     ( WS-ITEM-TS               )              
VIC                 NUMITEMS ( WS-NUM-ITENS-TS          )              
VIC                 NOHANDLE                                           
VIC        END-EXEC                                                    
VIC        EXEC CICS WRITEQ TS                                       
VIC                  QNAME    ( WS-TS-ALTERACAO          )           
VIC                  FROM     ( WS-CONTEUDO-TS           )           
VIC                  LENGTH   ( LENGTH OF WS-CONTEUDO-TS )           
VIC                  NUMITEMS ( WS-NUM-ITENS-TS          )           
VIC                  NOHANDLE                                        
VIC        END-EXEC     
390SAT     EXEC CICS                                                    
390SAT          LINK PROGRAM(CT-ATE790)                                 
390SAT          COMMAREA(WS-DATOS)                                      
390SAT          LENGTH(LENGTH OF WS-DATOS)                              
390SAT     END-EXEC.                                                                                             
           EXEC CICS                                                
                LINK PROGRAM  (WS-PCSPOS10)                         
                COMMAREA      (WS-COMMAREA-PCSPOS10)                
                LENGTH        (LENGTH OF WS-COMMAREA-PCSPOS10)      
                SYNCONRETURN                                        
           END-EXEC.

           EXEC CICS                                                    
                START TRANSID(CT-PCX3) FROM (WS-AUX-DATPCX3)            
           END-EXEC.                                                    
390SAT     EXEC CICS HANDLE CONDITION                                   
390SAT          ENDDATA (9999-ERROR-CICS)                               
390SAT          ERROR   (9999-ERROR-CICS)                               
390SAT     END-EXEC.                                                    

390SAT     EXEC CICS HANDLE ABEND                                       
390SAT          LABEL  (9999-ERROR-CICS)                                
390SAT     END-EXEC.                            
             .
       0002-PROGRAMA-CALL.                                          
      *-----------------------.                                         
           CALL CT-PCSPSD49        USING    LOG-REGISTRO                
           CALL CT-ATRG005 USING WS-ATCMBDIV                        
           CALL  CT-ATR980   USING   ATFINPGM.  
           CALL 'MQOPEN' USING W03-HCONN                                
                               MQOD                                     
                               W03-OPTIONS                              
                               W03-HOBJ-CLIENTQ                         
                               W03-COMPCODE                             
                               W03-REASON.                              

       0001-INSERT-TABELA.                                      
      *--------------------------.                                      

           EXEC SQL                                                     
                INSERT INTO MPDT083                                     
                       (NUMSEC,                                         
                        INDICA,                                         
                        PROGRAMA,                                       
                        CADENA,                                         
                        DATOS)                                          
                VALUES                                                  
                       (:TB-083NUMSEC,                                  
                        :TB-083INDICA,                                  
                        :TB-083PROGRAMA,                                
                        :TB-083CADENA,                                  
                        :TB-083DATOS)                                   
                    FOR :IND-TB083 ROWS                                 
           END-EXEC.          