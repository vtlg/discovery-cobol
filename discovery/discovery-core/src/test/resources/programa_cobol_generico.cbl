                                                                        
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

            EXEC SQL                                                     
                SELECT  T743.CODPORIEMI                                 
                INTO :DCLMPDT743.CODPORIEMI                             
                FROM PCS.MPDT743 T743                                   
                   INNER JOIN PCS.MPDT007 T007 ON                       
                           T743.CODENT    = T007.CODENT                 
                   AND     T743.PRODUCTO  = T007.PRODUCTO               
                   AND     T743.SUBPRODU  = T007.SUBPRODU               
                   AND     T743.CODCOSIF  = 3                           
                   INNER JOIN PCS.MPDT013 T013 ON                       
                                 T007.CODENT    = T013.CODENT           
                           AND   T007.CENTALTA  = T013.CENTALTA         
                           AND   T007.CUENTA    = T013.CUENTA           
                   INNER JOIN PCS.MPDT414 T414 ON                       
                               T013.CODENT    = T414.CODENT             
                       AND     T013.IDENTCLI  = T414.IDENTCLI           
                WHERE   T007.CODENT    = :WS-CODENT-GDA                 
                   AND  T007.CENTALTA  = :WS-CENTALTA-GDA               
                   AND  T007.CUENTA    = :WS-CUENTA-GDA                 
                   AND  T013.CALPART   = 'TI'                           
                WITH UR                                                 
               END-EXEC. 
            
            EXEC SQL                                                    
                 SELECT MAX(A.FECFAC)                                   
                   INTO :DCLMPDT251.FECFAC                              
                   FROM PCS.MPDT251 A                                   
                   JOIN PCS.MPDT044 B                                   
                     ON A.TIPOFAC = B.TIPOFAC                           
                    AND A.INDNORCOR = B.INDNORCOR                       
                  WHERE A.CODENT    = :DCLMPDT251.CODENT                
                    AND A.CENTALTA  = :DCLMPDT251.CENTALTA              
                    AND A.CUENTA    = :DCLMPDT251.CUENTA                
                    AND A.CLAMON    = :DCLMPDT251.CLAMON                
                    AND B.TIPOFACSIST = 67                              
                    AND B.SIGNO = '-'                                   
                    AND B.INDFACINF = 'N'                               
                   WITH UR                                              
            END-EXEC.    

               EXEC SQL                                                     
                SELECT T9.PAN,                                          
                       T178.CODBLQ,                                     
                       T178.TEXBLQ                                      
                 INTO :DCLMPDT009.PAN,                                  
                      :DCLMPDT178.CODBLQ :WS-CODBLQ-NULO ,              
                      :DCLMPDT178.TEXBLQ :WS-TEXBLQ-NULO                
                 FROM MPDT009 T9                                        
                 LEFT OUTER JOIN                                        
                       MPDT178 T178                                     
                    ON                                                  
                       T9.CODENT    = T178.CODENT                       
                   AND T9.CENTALTA  = T178.CENTALTA                     
                   AND T9.CUENTA    = T178.CUENTA                       
                 WHERE T9.CODENT    = :DCLMPDT009.CODENT                
                   AND T9.CENTALTA  = :DCLMPDT009.CENTALTA              
                   AND T9.CUENTA    = :DCLMPDT009.CUENTA                
                   AND T9.INDULTTAR = 'S'                               
                   AND T9.NUMBENCTA = 1                                 
           END-EXEC.    

           EXEC SQL                                                     
              DECLARE CUR_402_013 CURSOR FOR                            
                SELECT                                                  
                    B.CODENT,                                           
                    B.CENTALTA,                                         
                    B.CUENTA,                                           
                    B.NUMBENCTA,                                        
                    B.CALPART,                                          
                    B.FECBAJA                                           
                FROM  MPDT402 A                                         
                JOIN  MPDT013 B                                         
                  ON  A.CODENT  = B.CODENT                              
               AND  A.IDENTCLI  = B.IDENTCLI                            
               AND  A.CENTALTA  = B.CENTALTA                            
               AND  A.CUENTA    = B.CUENTA                              
               WHERE                                                    
                    A.CODENT    = :DCLMPDT402.CODENT                    
               AND  A.IDENTCLI  = :DCLMPDT402.IDENTCLI                  
               AND  A.TIPCONT   = :DCLMPDT402.TIPCONT                   
               AND  B.FECBAJA   = :CT-FECINI                            
             ORDER BY B.CODENT,B.CENTALTA,B.CUENTA,B.NUMBENCTA          
             FETCH FIRST 16 ROWS ONLY                                   
             OPTIMIZE FOR 1 ROW                                         
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