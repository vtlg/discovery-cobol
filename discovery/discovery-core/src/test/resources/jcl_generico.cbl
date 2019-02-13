//PCS3C650 JOB (PCS,BR,72591,00,00),'D%%ODATE',CLASS=F,MSGCLASS=O,      
//             MSGLEVEL=(1,1),REGION=0K,TIME=NOLIMIT,COND=(4,LT)        
//*                                                                     
//* %%GLOBAL GLOBAPCS                                                   
/*XEQ %%XEQ                                                             
//*                                                                     
//*                                                                     
/*ROUTE PRINT %%ROUTE                                                   
//*                                                                     
//* ********************************************************************
//* * JOBNAME ORIGINAL: PCS3C650                                       *
//* * FUNCAO.....:                                                     *
//* * PERIOD.....: DIARIO                                              *
//* * SCHEDULE...:                                                     *
//* * IMPACT_CRIACAO:                                                  *
//* * DATA_CRIACAO..:                                                  *
//* * UNID_RESPONSAVEL: MACIEL/LUIZ ERNESTO                            *
//* * UNID_CASO_ERRO..:                                                *
//* * DEPENDENCIAS:                                                    *
//* * OBSERVACOES:                                                     *
//* * ALTERACOES.:                                                     *
//* *   DATA   | RESPONS | IMPACT | BREVE TGECRICAO                    *
//* *----------|---------|--------|------------------------------------*
//* * DD/MM/AA | C999999 | 888888 |                                    *
//* *----------|---------|--------|------------------------------------*
//* ********************************************************************
//*                                                                     
//JOBLIB   DD  DISP=SHR,                                                
//             DSN=%%LOAD1                                              
//         DD  DISP=SHR,                                                
//             DSN=DB2.%%DB2GRP.SDSNLOAD                                
//         DD  DISP=SHR,                                                
//             DSN=CEE.SCEERUN                                          
//         DD  DISP=SHR,                                                
//             DSN=DB2.%%DB2GRP.SDSNEXIT                                
//         DD  DISP=SHR,                                                
//             DSN=%%LOAD2                                              
//*                                                                     
//* %%LIBSYM %%SIMBL %%MEMSYM NACIONAL  
//*
//* %%SET %%VCNDC = IBM.CND.SDGALINK
//* %%SET %%CNT = PRD.V01.PROCESS.CNT.PLEX02
//* %%SET %%VCNDP = CND.P6                                
//*                                                                     
//STEP01   EXEC PGM=IDCAMS,                                             
//             COND=(4,LT)                                              
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//*                                                                     
//*   ENTRADA - ARQUIVO DE REMESSA SIMPLES                              
//INDIN    DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.MDA1B4C2.B446.S03.FRESPOK      
//         DD  DISP=SHR,
//             DSN=ZIP.LOAD
//INDD     DD  DISP=SHR,
//             DSN=NDS.CTM.V01.PCS.D%%ODATE.PLEX02
//NETRC    DD  DISP=SHR,
//             DSN=TCP.NETRC.PLEX%%AMB
//*                                                                     
//*   SAIDA   - BACKUP DO ARQUIVO DE REMESSA SIMPLES                    
//INDOUT   DD  DISP=(,CATLG,DELETE),                                    
//             DSN=%%ALIAS%%.PCS.MZ.BDT2.PCS650.ST10.D%%ODATE,          
//             UNIT=SYSDA,                                              
//             SPACE=(TRK,(10,10),RLSE),                                
//             DCB=(RECFM=FB,LRECL=200)                                 
//SYSIN    DD  *                                                        
 REPRO IFILE(INDIN) OFILE(INDOUT)                                       
//*
//*                                                                     
//STEP02   EXEC PGM=IEFBR14                                             
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//*                                                                     
//DD01     DD  DISP=(MOD,DELETE),                                       
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT,        
//             UNIT=3390,                                               
//             SPACE=(TRK,(5,2),RLSE)                                   
//*                                                                     
//DD02     DD  DISP=(MOD,DELETE),                                       
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE,    
//             UNIT=3390,                                               
//             SPACE=(TRK,(5,2),RLSE)                                   
//*                                                                     
//DD03     DD  DISP=(MOD,DELETE),                                       
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.JBD22.PBD08.DS4B0.D%%ODATE,    
//             UNIT=3390,                                               
//             SPACE=(TRK,(5,2),RLSE)                                   
//*                                                                     
//DD04     DD  DISP=(MOD,DELETE),                                       
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.JBD22.PBD08.DS102.D%%ODATE,    
//             UNIT=3390,                                               
//             SPACE=(TRK,(5,2),RLSE)                                   
//*                                                                     
//STEP03   EXEC PGM=IKJEFT1A,                                           
//             COND=(0,NE),                                             
//             DYNAMNBR=20                                              
//*                                                                     
//*ENTRADA - ARQUIVO DE CARTAS PIN - PC01                               
//ECSDS4B0 DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.MC01B473.B473.S02.FSUNEC01     
//*                                                                     
//*SAIDA   - ARQUIVO DE CARTA + INFORMACOES DE CEP                      
//SCSDS4B0 DD  DISP=(,CATLG,DELETE),                                    
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE,    
//             UNIT=3390,                                               
//             SPACE=(TRK,(20,10),RLSE),                                
//             DCB=(RECFM=FB,LRECL=3250)                                
//SYSTSPRT DD  SYSOUT=*                                                 
//SYSPRINT DD  SYSOUT=*                                                 
//SYSUDUMP DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSTSIN  DD  *                                                        
  DSN SYSTEM(%%DB2GRP)                                                  
  RUN  PROGRAM(PCSPBD06) PLAN(PCSBATCH)                                 
//*                                                                     
//*                                                                     
//STEP04   EXEC PGM=SORT,                                               
//             COND=(0,NE)                                              
//SORTIN   DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE     
//*                                                                     
//SORTOUT  DD  DISP=(,CATLG,DELETE),                                    
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT,        
//             UNIT=3390,                                               
//             SPACE=(TRK,(20,10),RLSE),                                
//             DCB=(RECFM=FB,LRECL=3250)                                
//SYSIN    DD  *                                                        
  SORT FIELDS=(3191,09,CH,A,        ** NU-CENTRALIZADORA                
               3211,09,CH,A,        ** NU-ETIQUETA-AMRDO                
               3201,10,CH,A,        ** CEP                              
                 13,01,CH,A,        ** C4C6-TIPOREG                     
                 32,28,CH,A,        ** C4C6-CLAVE                       
                 69,02,CH,A)        ** C4C6-TIPREG-DETALLE              
//*                                                                     
//SYSOUT   DD  SYSOUT=*                                                 
//SYSPRINT DD  SYSOUT=*                                                 
//*                                                                     
//*                                                                     
//STEP05  EXEC PGM=IDCAMS                                              
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSIN    DD  *                                                        
  DELETE  %%ALIAS%%.PCS.MZ.BZX0.MOUTSORT.MONE.S01.FPAG0001              
  IF MAXCC=8 THEN SET MAXCC=0                                           
//*                            
//STEP06   EXEC PGM=IDCAMS                                              
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSIN    DD  *                                                        
   DELETE  %%ALIAS%%.PCS.MZ.BZX0.MDA1B4Q7.C761.S01.SELECT               
   DELETE  %%ALIAS%%.PCS.MZ.BZX0.MDA1BD5C.C761.S02.SELECT               
   DELETE  %%ALIAS%%.PCS.MZ.BZX0.MDA1BD5C.C761.S02.SIERED5D             
   DELETE  %%ALIAS%%.PCS.MZ.BZX0.MDA1BD5D.C761.S02.SELECT               
   DELETE '%%ALIAS%%.PCS.MZ.BZX0.MPJ32100.FCCON001'
  IF MAXCC=8 THEN SET MAXCC=0                                           
//SYSIN22  DD  *
  LISTCAT ENTRIES(CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.USD.D%%DTANT)
  IF MAXCC=4 THEN SET MAXCC=01
//*
//SYSIN23   DD  *
  LISTCAT ENTRIES(CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.BRL.D%%DTANT)
  IF MAXCC=4 THEN SET MAXCC=01
//*
//*                                                                     
//STEP07   EXEC PGM=ECEPB609,                                           
//             COND=(0,NE),                                             
//             DYNAMNBR=20,                                             
//             REGION=4096K,                                            
//             PARM='***-****-********-***-******-******'               
//*====>          SUREG-UNID-DTAMOV  -SIS-RELAT.-PAGINA                 
//ECED1032 DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE     
//ECED1041 DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.FDL.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE     
//ECED1901 DD  DISP=(NEW,CATLG,DELETE),                                 
//             DSN=%%ALIAS%%.PCS.MZ.BHX0.PBD5D.RELAT.D%%ODATE,          
//             UNIT=3390,                                               
//             SPACE=(CYL,(5,3),RLSE),                                  
//             DCB=(RECFM=FB,BLKSIZE=0,LRECL=133)                       
//SYSDBOUT DD  SYSOUT=*                                                 
//SYSTSPRT DD  SYSOUT=*                                                 
//SYSABOUT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSPRINT DD  SYSOUT=*                                                 
//SYSIN    DD  DUMMY                                                    
//*                                                                     
//*  %%IF %%AMB EQ P6                                                   
//*                                                                     
//*                                                                     
//STEP08  EXEC PGM=ICEGENER,                                           
//             COND=(0,NE)                                              
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSIN    DD  DUMMY                                                    
//SYSUT1   DD  DUMMY,                                                   
//             UNIT=3390,                                               
//             SPACE=(TRK,(20,2),RLSE),                                 
//             DCB=(RECFM=FB,BLKSIZE=0,LRECL=080)                       
//SYSUT2   DD  DISP=(,CATLG,DELETE),                                    
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.MRJT0201.MIGRACAO.ACUM,        
//             UNIT=3390,                                               
//             SPACE=(TRK,(200,20),RLSE),                               
//             DCB=(RECFM=FB,BLKSIZE=0,LRECL=080)                       
//*                                               
//STEP09 EXEC PGM=DMBATCH,                                            
//             COND=(00,NE),                                            
//             PARM=(YYSLY)                                             
//STEPLIB  DD  DISP=SHR,                                                
//             DSN=%%VCNDC                                              
//DMPUBLIB DD  DISP=SHR,                                                
//             DSN=%%CNT                                                
//DMMSGFIL DD  DISP=SHR,                                                
//             DSN=%%VCNDP%%..MSG                                       
//DMNETMAP DD  DISP=SHR,                                                
//             DSN=%%VCNDP%%..NETMAP                                    
//DMNETMAP DD  DISP=SHR,                                                
//             DSN=%%VCNDT%%..NETMAP                                    
//DMPRINT  DD  SYSOUT=*                                                 
//NDMCMDS  DD  SYSOUT=*                                                 
//SYSPRINT DD  SYSOUT=*                                                 
//SYSUDUMP DD  SYSOUT=*                                                 
//SYSIN    DD  *                                                        
   SIGNON                                                               
   SUBMIT   PROC=CNTPCSSP -                                             
          &DSN1=%%ALIAS%%.PCS.MZ.BDN2.MA03B414.B417.S16.D%%ODATE -      
          &DSN2=CNT.PCS.MZ.BGT1.IGAP0323.D%%ODATE                       
 SIGNOFF                                                                
//*                                                                     
//  IF IEBPTPCH.RC NE 0 THEN                                            
//*                                                                     
//* %%SET %%DD = %%SUBSTR %%ODATE 5 2                                   
//* %%SET %%MM = %%SUBSTR %%ODATE 3 2                                   
//*                                                                     
//STEP10 EXEC CTMAPI                                                  
//DAPRINT  DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//SYSIN    DD  *                                                        
COND ADD COND GDBUR-ELO-SUGAP0323 %%DD%%.%%MM                           
/*                                                                      
//  ENDIF                                                               
//*                                                                     
//STEP11  EXEC PGM=PCSBT663,                                           
//             COND=(0,NE)                                              
//SYSIN    DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.DATA.PROCBAT.D%%ODATE          
//BT663L1  DD  DISP=SHR,                                                
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.P3C010.ARQCTBL.D%%ODATE        
//BT663G1  DD  DISP=(NEW,CATLG,DELETE),                                 
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.BT663.MC.D%%ODATE,             
//             UNIT=3390,                                               
//             SPACE=(TRK,(1000,500),RLSE),                             
//             DCB=(RECFM=FB,DSORG=PS)                                  
//SYSTSPRT DD  SYSOUT=*                                                 
//SYSPRINT DD  SYSOUT=*                                                 
//SYSOUT   DD  SYSOUT=*                                                 
//*                                      
//STEP12  EXEC PGM=IKJEFT01,                                           
//             COND=(5,LE),                                             
//             DYNAMNBR=200                                             
//STEPLIB  DD  DISP=SHR,                                                
//             DSN=DB2.%%DB2GRP.RUNLIB.LOAD                             
//SYSTSPRT DD  SYSOUT=*                                                 
//SYSTSIN  DD  DISP=SHR,                                                
//             DSN=%%CARDLIB(UNLOAD)                                    
//SYSPRINT DD  SYSOUT=*                                                 
//SYSUDUMP DD  SYSOUT=*                                                 
//SYSREC00 DD  DISP=(NEW,CATLG,DELETE),                                 
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.M001B907.B907.S05.FTAB58,      
//             UNIT=SYSDA,                                              
//             SPACE=(TRK,(50,10),RLSE),                                
//             DCB=(RECFM=FB,BLKSIZE=0,LRECL=40,DSORG=PS)               
//SYSPUNCH DD  DUMMY                                                    
//SYSIN    DD  *                                                        
       SELECT CO_ENTIDADE,                                              
              CO_AGENCIA_INCLUSAO,                                      
              CO_CONTA_CONTRATO,                                        
              NU_ANO_EXTRATO                                            
       FROM PCS.PCSTBD58_EXTRATO_ANUAL;                                 
//*
//STEP13 EXEC PGM=IKJEFT01,                                           
//             DYNAMNBR=200                                             
//STEPLIB  DD DSN=DB2.%%DB2GRP.RUNLIB.LOAD,DISP=SHR                     
//SYSTSPRT DD SYSOUT=*                                                  
//SYSTSIN  DD DSN=%%CARDLIB(UNLOAD),DISP=SHR                            
//SYSPRINT DD  SYSOUT=*                                                 
//SYSUDUMP DD  SYSOUT=*                                                 
//SYSREC00 DD  DISP=(,CATLG,DELETE),                                    
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.PCS1S302.MPDT174,              
//             UNIT=3390,                                               
//             SPACE=(CYL,(50,10),RLSE),                                
//             DCB=(RECFM=FB,DSORG=PS)                                  
//SYSPUNCH DD  DUMMY                                                    
//SYSIN    DD  *                                                        
  SELECT CODENT                                                         
       , CENTALTA                                                       
       , CUENTA                                                         
       , NUMSECHIS                                                      
       , PAN                                                            
       , CODCOM                                                         
       , CHAR(FECHAMOD,ISO)                                             
       , HORAMOD                                                        
       , TIPACCES                                                       
       , PROCESO                                                        
       , DESPROCESO                                                     
       , DATOMOD                                                        
       , MOTIVO                                                         
       , CANALMOD                                                       
       , CODENTUMO                                                      
       , CODOFIUMO                                                      
       , USUARIOUMO                                                     
       , CODTERMUMO                                                     
       , CONTCUR                                                        
       , TABLA                                                          
       , REGANT                                                         
       , REGACT                                                         
  FROM PCS.MPDT068                                                      
  WHERE MOTIVO   IN   ('RO','RM', 'MB' ,'MM','MO')                      
    AND TABLA    IN  ('174')                                            
   ORDER BY CODENT, CENTALTA, CUENTA, NUMSECHIS                         
   WITH UR;                                                             
/*                                                                      
//*
//*
//* Novas inclusões a partir daqui
//*
//*
//STEP14 EXEC PGM=DMBATCH,
//             PARM=(YYSLY),COND=(0,NE)
//SYSIN    DD  *
 SIGNON
            SNODE=I1CNDCEF -
            &DSN1=%%ALIAS%%.PCS.MZ.BDS2.RETOREL1.PCS3C014.D%%ODATE  -
            &DSN2=CNT.PCS.MZ.BDS2.INELO104.OS3C014.D%%ODATE
 SIGNOFF
//*
//SYSIN2   DD  *
   SIGNON
            SNODE=HCNDCEF -
             &DSN1=PCS.MZ.BDA2.PC833.S1DQ831.D%%ODATE -
             &DSN2=CNT.FIN.MZ.BDC2.IPCSQ831.D%%ODATE
   SIGNOFF
//*
//SYSIN3   DD  *
 SIGNON
           &DSN1=%%ALIAS%%.PCS.MZ.BZX0.MA01B424.B496.S02.FSOLPIN1 -
          &DSN2=%%ALIAS%%.CNT.RAN.MZ.BAT1.IPCS4A8E.D%%ODATE
 SIGNOFF
//*
//SYSIN4   DD  *
 SIGNON
          &DSN1=%%ALIAS%%.PCS.MZ.BDN2.MA02B414.B417.S22.D%%ODATE -
          &DSN2=CNT.PCS.MZ.BGT1.IGAP0224.D%%ODATE
 SIGNOFF
//*
//SYSIN5   DD  *
 SIGNON
          &DSN1=%%ALIAS%%.PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE.ZIP -
          &DSN2=CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE.ZIP
//SYSIN7   DD  *
          &DSN1=%%ALIAS%%.PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE -
          &DSN2=CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE
 SIGNOFF
//*
//SORTOUT1 DD  DSN=CNT.PCS.MZ.BDS2.INELOAGL.RE3EMISS.D%%ODATE,
//             DISP=(,CATLG,DELETE),
//             UNIT=3390,
//             SPACE=(TRK,(5,1),RLSE),
//             DCB=(RECFM=FB,LRECL=364,DSORG=PS)
//*
//*-------------------------------------------------------------------*
//PBELIL1  DD  DISP=SHR,
//             DSN=CNT.PCS.MZ.BDS2.INELOAGE.R2EMISS.D%%ODATE
//*-------------------------------------------------------------------*
//PBELIG1  DD  DISP=(,CATLG,DELETE),
//             DSN=%%ALIAS%%.PCS.MZ.BDS2.AGEN.LIQD.D%%ODATE,
//             UNIT=3390,
//             SPACE=(TRK,(5,1),RLSE),
//             DCB=(RECFM=FBA,LRECL=499,DSORG=PS)
//*-------------------------------------------------------------------*
//*-------------------------------------------------------------------*
//*
//*
//INDIN2    DD  DISP=SHR,
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.IOCR0424.JCB00024
//         DD  DISP=SHR,
//             DSN=CNT.PCS.MZ.BDC2.IOCR0424.D%%ODATE
//*
//INDOUT2   DD  DISP=(NEW,CATLG,DELETE),
//             DSN=%%ALIAS%%.PCS.MZ.BZX0.IOCR0424.JCB00024.TRABALHO,
//             UNIT=3390,
//             RECFM=FB,
//             LRECL=7000,
//             SPACE=(CYL,(50,200),RLSE)
//SYSIN7   DD  *
 SIGNON
            SNODE=SCNDCEF -
            &DSN1=PCS.MZ.BDB2.P2C808.DS897.LTE020.D%%ODATE -
            &DSN2=CNT.CAC.MZ.BAT1.IPCSM897.D%%ODATE
 SIGNOFF
//*
//SYSIN8   DD  *
BLT
MEM-OVERWRITE=FORCE(%%ODATE)
LIBRARY=DUMMY
TABLE=DUMMY
MEMNAME=PCS01GDB
MODEL=(END.V01.SCHEDULE.PLEX02,PCS01GDB,PCS01GDB)
DESC='ARQUIVO: %%ALIAS%%.PCS.MZ.BHX0.P393.ANALIT.BOLETO.D%%ODATE'
%%RESOLVE NO
    GROUP=P393.ANALIT.BOLETO
    SETVAR=%%SISTDEST=SIPUB
    SETVAR=%%MAQUINA=DF7436SR081
    SETVAR=%%DSN2=SIPUBPCS.MZ.BHX0.P393.ANALIT.BOLETO.D%%ODATE
%%RESOLVE YES
    CONFIRM=N
//*
%%ENDIF
//*
//SYSUT29   DD  DSN=CNT.PCS.MZ.BDC2.ICAIXSEG.D%%ODATE,
//             DISP=(,CATLG,DELETE),UNIT=3390,
//             DCB=*.SYSUT1,
//             SPACE=(TRK,(1,1),RLSE)
//STEPLIB1 DD  DISP=SHR,
//             DSN=CEE.SCEERUN
//SYSUT26  DD  DISP=(NEW,CATLG,DELETE),
//             DSN=PRD0.PCS.MZ.BDQ2.PC015.DS114.D%%ODATE.GDB,
//             UNIT=3390,
//             SPACE=(%%SPACE091,RLSE),
//             DCB=*.SYSUT1
//RELIN20  DD DSN=CNT.PCS.MZ.BDS2.IJCB.SETTSRPT.BRL.D%%DTANT,DISP=SHR
//*                                                                     07660905
//E1DQ0000 DD  DISP=SHR,
//             DSN=COD.PCS.V00.SAT.MPDT999.UNLOAD
//*