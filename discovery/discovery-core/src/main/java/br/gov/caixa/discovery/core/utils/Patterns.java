package br.gov.caixa.discovery.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {

	//("^.*[\\s]{1,}PROGRAM-ID[\\.]{1,1}[\\s]{1,}(?<nomePrograma>[\\S]{1,})[\\s]{0,}[\\.]{1,}[\\s]{0,}$")
	
	public static final Pattern INJETOR_P_DSN_CARDLIB = Pattern.compile("^.*\\((?<cardlib>[\\S]{1,}?)\\).*$");
	
	public static final Pattern EXTRATOR_P_NOME_ARTEFATO = Pattern.compile("^.*\\((?<parametro>[\\S]{1,}?)\\).*$");

	

	public static void main(String[] args) {
		Matcher m = INJETOR_P_DSN_CARDLIB.matcher(
				"'DES.PCS.V00.C083347.FONTES.COBLE(PCSPRO11)'");
		if (m.matches()) {
			System.out.println(m.group("cardlib"));
			System.out.println("Aqui");
		} else {
			System.out.println("Não");
		}
	}
	
	public static final Pattern JCL_P_JOBNAME = Pattern.compile("^.*[/]{2,2}(?<jobName>[\\S]{1,})[\\s]{1,}JOB.*$");

	public static final Pattern JCL_P_DSN_DELETE = Pattern
			.compile("^.*DELETE[\\s]{1,}(?<dsn>[\\S&&[^,]]{1,})[,|\\s]{0,}.*$");

	public static Pattern JCL_P_PGM_1 = Pattern.compile(
			"^.*[/]{1,2}(?<nomeStep>[\\S]{1,})[\\s]{1,}EXEC[\\s]{1,}PGM=(?<programa>[\\S&&[^,]]{1,})[\\s]{0,}.*$");

	public static Pattern JCL_P_PROC_1 = Pattern.compile(
			"^.*[/]{1,2}(?<nomeStep>[\\S]{1,})[\\s]{1,}EXEC[\\s]{1,}PROC=(?<programa>[\\S&&[^,]]{1,})[\\s]{0,}.*$");

	public static Pattern JCL_P_EXEC_1 = Pattern
			.compile(".*[/]{2,2}(?<nomeStep>[\\S]{1,})[\\s]{1,}EXEC[\\s]{1,}(?<programa>[\\S&&[^,]]{1,})[\\s]{0,}.*$");

	public static Pattern JCL_P_DD_INICIO_2 = Pattern.compile("^.* DD .*$");

	public static final Pattern JCL_P_DSN = Pattern.compile("^.*DD.*DSN=(?<dsn>[\\S&&[^,]]{1,})[,|\\s]{1,}.*$");

	public static final Pattern JCL_P_DSN_NUMERO_FIND = Pattern
			.compile("DSN[0-9]{1,}=(?<dsn>[\\S&&[^,]]{1,})[,|\\s]{1,}");
	public static final Pattern JCL_P_DSN_NUMERO = Pattern
			.compile("^.*DSN[0-9]{1,}=(?<dsn>[\\S&&[^,]]{1,})[,|\\s]{1,}.*$");
	public static final Pattern JCL_P_DISP_SHR = Pattern.compile("^.*DD.*DISP=SHR.*$");
	public static final Pattern JCL_P_DISP_COMPLETO = Pattern.compile("^.*DD.*DISP=\\((?<parametros>.*?)\\).*$");

	public static final Pattern JCL_P_DSN_DELETE_PARENTESES = Pattern
			.compile("^.*DELETE[\\s]{1,}\\([\\s]{0,}(?<dsn>[\\S&&[^,]]{1,})[\\s]{0,}\\)[,|\\s]{0,}.*$");
	// final Pattern P_DSN_CARDLIB =
	// Pattern.compile("^.*DSN=%%CARDLIB[\\s]{0,}\\((?<cardlib>[\\S&&[^,]]{1,})\\).*$");

	public static final Pattern JCL_P_DSN_LISTC_ENTRIES = Pattern
			.compile("^.*[\\s]{1,}(LISTC|LISTCAT)[\\s]{1,}(ENTRIES|ENT|LVL|LEVEL)\\((?<dsn>[\\S&&[^,]]{1,})[)]{1,}.*$");

	public static final Pattern JCL_P_IDENTIFICADOR = Pattern.compile("^[/]{0,2}(?<identificador>.*?)DD.*$");

	public static final Pattern JCL_P_NOME_TIRA_DATA_FIXA = Pattern.compile("^(?<nome>[\\S]{1,}).D[0-9]{4,8}.*$");
	public static final Pattern JCL_P_NOME_TIRA_AMBIENTE_FIXO = Pattern
			.compile("^[PRE||[REL]||[DES]||[TQS]||[HMP]||[PRD]]{3,3}.(?<nome>[\\S]{1,})$");

	public static final Pattern JCL_P_DELETE_DSN = Pattern.compile("DELETE[\\s]{1,}(?<dsn>[\\S]{1,})");

	public static final Pattern JCL_P_RUN_PROGRAMA_1 = Pattern.compile(
			"^.*[\\s]{0,}RUN[\\s]{1,}PROGRAM[\\s]{0,}[(]{1,1}(?<programa>[\\S&&[^,]]{1,}?)[)]{1,1}[\\s]{0,}.*$");
	public static final Pattern JCL_P_SUBMIT_PROC_1 = Pattern
			.compile("^.*[\\s]{0,}SUBMIT[\\s]{1,}PROC=(?<programa>[\\S&&[^,]]{1,})[\\s]{0,}.*$");

	public static final Pattern JCL_P_INSERT_1_INICIO = Pattern
			.compile("^[\\s]{0,}INSERT[\\s]{1,}INTO[\\s]{1,}(?<tabela>[\\S&&[^,]]{1,})[\\s]{0,}.*$");
	public static final Pattern JCL_P_INSERT_1_FIM = Pattern.compile("^.*[\\s]{0,}[\\s]{0,};[\\s]{0,}.*$");

	public static final Pattern JCL_P_DELETE_1_INICIO = Pattern
			.compile("^[\\s]{0,}DELETE[\\s]{1,}FROM[\\s]{1,}(?<tabela>[\\S&&[^,]]{1,})[\\s]{0,}.*$");
	public static final Pattern JCL_P_DELETE_1_FIM = Pattern.compile("^.*[\\s]{0,};[\\s]{0,}.*$");

	public static final Pattern JCL_P_SELECT_1_INICIO = Pattern.compile("^[\\s]{0,}SELECT[\\s]{0,}.*$");
	public static final Pattern JCL_P_SELECT_1_FIM = Pattern.compile("^.*[\\s]{0,};[\\s]{0,}.*$");

	public static final Pattern JCL_P_INSERT = Pattern
			.compile("^[\\s]{0,}INSERT[\\s]{1,}INTO[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*$");

	public static final Pattern JCL_P_DELETE = Pattern
			.compile("^[\\s]{0,}DELETE[\\s]{1,}FROM[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*$");

	public static final Pattern JCL_P_SELECT = Pattern
			.compile("^[\\s]{0,}SELECT[\\s]{1,}.{1,}[\\s]{1,}FROM[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*$");

	public static final Pattern JCL_P_JOIN = Pattern
			.compile("[\\s]{1,}INNER[\\s]{1,}JOIN[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}");

	/*
	 *  
	 */

	public static Pattern COBOL_P_PROGRAM_ID_1 = Pattern
			.compile("^.*[\\s]{1,}PROGRAM-ID[\\.]{1,1}[\\s]{1,}(?<nomePrograma>[\\S]{1,})[\\s]{0,}[\\.]{1,}[\\s]{0,}$");
	public static Pattern COBOL_P_PROGRAM_ID_2 = Pattern
			.compile("^.*[\\s]{1,}PROGRAM-ID[\\.]{1,1}[\\s]{1,}(?<nomePrograma>[\\S]{1,})[\\s]{0,}[\\.]{0,}.*$");
	public static Pattern COBOL_P_PROGRAM_ID_3 = Pattern.compile(
			"^.*[\\s]{1,}PROGRAM-ID[\\s]{1,}[\\.]{1,1}[\\s]{1,}(?<nomePrograma>[\\S]{1,})[\\s]{0,}[\\.]{0,}.*$");

	public static Pattern COBOL_P_IDENTIFICATION_DIVISION = Pattern
			.compile("^[\\.]{0,}[\\s]{1,1}IDENTIFICATION[\\s]{1,}DIVISION[\\s]{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_ENVIRONMENT_DIVISION = Pattern
			.compile("^[\\.]{0,}[\\s]{1,}ENVIRONMENT[\\s]{1,}DIVISION[\\s]{0,}[\\.]{1,1}[\\s]{0,}.*$");

	public static Pattern COBOL_P_DATA_DIVISION = Pattern
			.compile("^[\\.]{0,}[\\s]{1,1}DATA[\\s]{1,}DIVISION[\\s]{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_PROCEDURE_DIVISION = Pattern
			.compile("^[\\.]{0,}[\\s]{1,1}PROCEDURE[\\s]{1,}DIVISION[\\.]{0,}.{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_PARAGRAFO = Pattern
			.compile("^[\\s]{1,1}(?<nomeParagrafo>[\\S]{1,})[\\s]{0,}.{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_PARAGRAFO_EXIT = Pattern
			.compile("^[\\s]{1,1}(?<nomeParagrafo>[\\S]{1,})[\\s]{0,}[\\.]{1,1}[\\s]{1,}EXIT.*$");

	public static Pattern COBOL_P_FILE_CONTROL_INICIO = Pattern
			.compile("^.*[\\s]{0,}FILE-CONTROL[\\s]{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_SELECT_INICIO = Pattern.compile("^.*[\\s]{1,}(?<comando>SELECT[\\s]{1,}.*)$");

	public static Pattern COBOL_P_SELECT_FIM = Pattern.compile("^.*[\\.]{1,1}[\\s]{0,}.*$");

	public static Pattern COBOL_P_IDENTIFICADOR_OPTIONAL = Pattern
			.compile("^SELECT OPTIONAL[\\s]{1,1}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_IDENTIFICADOR = Pattern.compile("^SELECT[\\s]{1,1}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_ASSIGN = Pattern.compile("^.*ASSIGN[\\s]{1,1}(?<parametro>[\\S&&[^\\.]]{1,}).*");
	public static Pattern COBOL_P_ASSIGN_TO = Pattern
			.compile("^.*ASSIGN TO[\\s]{1,1}(?<parametro>[\\S&&[^\\.]]{1,}).*");
	public static Pattern COBOL_P_ORGANIZATION_IS = Pattern
			.compile("^.*ORGANIZATION IS[\\s]{1,1}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_ACCESS_MODE = Pattern.compile("^.*ACCESS MODE IS[\\s]{1,1}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_RECORD_KEY_IS = Pattern
			.compile("^.*RECORD KEY IS[\\s]{1,1}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_ALTERNATE_RECORD_KEY_IS = Pattern
			.compile("^.*ALTERNATE RECORD KEY IS[\\s]{1,1}(?<parametro>[\\S]{1,}).*");

	public static Pattern COBOL_P_FILE_SECTION = Pattern
			.compile("^.*[\\s]{0,}FILE[\\s]{1,}SECTION[\\s]{0,}[\\.]{1,1}[\\s]{0,}$");

	public static Pattern COBOL_P_FD_INICIO = Pattern.compile("^.*[\\s]{1,}(?<comando>FD[\\s]{1,}.*)$");

	public static Pattern COBOL_P_FD_FIM = Pattern.compile("^.*[\\.]{1,1}[\\s]{0,}.*$");

	public static Pattern COBOL_P_VARIAVEL_SEGUINTE = Pattern
			.compile("^[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S&&[^\\.]]{1,})[\\s]{0,}.*$");

	public static Pattern COBOL_P_COPY_SEGUINTE = Pattern
			.compile("^[\\s]{1,}COPY[\\s]{1,}(?<nomeChamado>[\\S[^\\.]]{1,})[\\s]{0,}[\\.]{1,}[\\s]{0,}$");

	public static Pattern COBOL_P_COPY_REPLACING_SEGUINTE = Pattern
			.compile("^.*[\\s]{1,}COPY[\\s]{1,}(?<nomeChamado>[\\S]{1,})[\\s]{1,}" + "REPLACING[\\s]{1,}"
					+ "[=]{2,2}[\\s]{0,}(?<valorAntigo>[\\S]{1,})[\\s]{0,}[=]{2,2}[\\s]{1,}" + "BY[\\s]{1,}"
					+ "[=]{2,2}[\\s]{0,}(?<novoValor>[\\S]{1,})[\\s]{0,}[=]{2,2}[\\s]{0,}" + "[\\.]{1,}[\\s]{0,}$");

	public static Pattern COBOL_P_PATTERN_REPLACING_SEM_IGUAL = Pattern
			.compile("^[\\s]{1,}COPY[\\s]{1,}" + "(?<nomeChamado>[\\S]{1,})[\\s]{1,}" + "REPLACING[\\s]{1,}"
					+ "==[\\s]{0,}(?<valorAntigo>[\\S]{1,})[\\s]{0,}==[\\s]{1,}" + "BY[\\s]{1,}"
					+ "(?<novoValor>[\\S]{1,})[\\s]{0,}[\\.]{1,}[\\s]{0,}$");

	public static Pattern COBOL_P_FD = Pattern.compile("^.*FD[\\s]{1,}(?<parametro>[\\S&&[^\\.]]{1,}).*");

	public static Pattern COBOL_P_RECORD_CONTAINS = Pattern
			.compile("^.*RECORD[\\s]{1,}CONTAINS[\\s]{1,}(?<parametro1>[\\S]{1,})[\\s]{1,}(?<parametro2>[\\S]{1,}).*");
	public static Pattern COBOL_P_RECORD_IS_VARYING = Pattern.compile("^.*RECORD[\\s]{1,}IS[\\s]{1,}VARYING.*");
	public static Pattern COBOL_P_RECORD_CONTAINS_N_TO_N = Pattern.compile(
			"^.*RECORD[\\s]{1,}CONTAINS[\\s]{1,}(?<parametro1>[\\S]{1,})[\\s]{1,}TO[\\s]{1,}(?<parametro2>[\\S]{1,}).*");
	public static Pattern COBOL_P_BLOCK_CONTAINS = Pattern
			.compile("^.*BLOCK[\\s]{1,}CONTAINS[\\s]{1,}(?<parametro1>[\\S]{1,})[\\s]{1,}(?<parametro2>[\\S]{1,}).*");
	public static Pattern COBOL_P_RECORD_MODE_IS = Pattern
			.compile("^.*RECORDING[\\s]{1,}MODE[\\s]{1,}IS[\\s]{1,}(?<parametro>[\\S]{1,}).*");
	public static Pattern COBOL_P_DATA_RECORD_IS = Pattern
			.compile("^.*DATA[\\s]{1,}RECORD[\\s]{1,}IS[\\s]{1,}(?<parametro>[\\S]{1,}).*");

	public static Pattern COBOL_P_VERBO_DELETE = Pattern.compile("^DELETE[\\s]{1,}(?!FROM).*");
	public static Pattern COBOL_P_VERBO_ARQUIVO = Pattern
			.compile("^(?<verbo>[\\S]{1,})[\\s]{1,}(?<arquivo>[\\S]{1,}).*$");

	public static Pattern COBOL_P_TABELA = Pattern
			.compile("^.*[\\s]{1,}(EXEC[\\s]{1,}SQL[\\s]{1,})INCLUDE[\\s]{1,}(?<nomeTabela>[\\S]{1,})[\\s]{1,}.*$");

	public static Pattern COBOL_P_COPY = Pattern
			.compile("^[\\s]{1,}COPY[\\s]{1,}(?<nomeChamado>[\\S]{1,})[\\s]{0,}[\\.]{1,}[\\s]{0,}$");
	public static Pattern COBOL_P_COPY_REPLACING_1 = Pattern
			.compile("^.*[\\s]{1,}COPY[\\s]{1,}" + "(?<nomeChamado>[\\S]{1,})[\\s]{1,}" + "REPLACING[\\s]{1,}"
					+ "[=]{2,2}[\\s]{0,}(?<valorAntigo>[\\S]{1,})[\\s]{0,}[=]{2,2}[\\s]{1,}" + "BY[\\s]{1,}"
					+ "[=]{2,2}[\\s]{0,}(?<novoValor>[\\S]{1,})[\\s]{0,}[=]{2,2}[\\s]{0,}" + "[\\.]{1,}[\\s]{0,}$");

	public static Pattern COBOL_P_UPDATE_1 = Pattern
			.compile(".*[\\s]{1,}UPDATE[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*");
	public static Pattern COBOL_P_DELETE_FROM_1 = Pattern
			.compile(".*[\\s]{1,}DELETE[\\s]{1,}FROM[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*");
	public static Pattern COBOL_P_INSERT_1 = Pattern
			.compile(".*[\\s]{1,}INSERT[\\s]{1,}INTO[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*");
	public static Pattern COBOL_P_FROM_WHERE_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>.*) WHERE.*");
	public static Pattern COBOL_P_FROM_INNER_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>.*) INNER.*");
	public static Pattern COBOL_P_FROM_JOIN_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>.*) JOIN.*");
	public static Pattern COBOL_P_FROM_LEFT_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>.*) LEFT.*");
	public static Pattern COBOL_P_FROM_RIGHT_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>.*) RIGHT.*");

	public static Pattern COBOL_P_FROM_1 = Pattern.compile(".*[\\s]{1,}FROM[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}.*");

	public static Pattern COBOL_P_EXEC_SQL_1 = Pattern.compile("^[\\s]{1,}EXEC[\\s]{1,}SQL[\\s]{0,}$");
	public static Pattern COBOL_P_EXEC_SQL_2 = Pattern.compile("^[\\s]{1,}EXEC[\\s]{1,}SQL[\\s]{1,}.*$");
	public static Pattern COBOL_P_END_SQL_1 = Pattern.compile("^.*[\\s]{1,}END-EXEC[\\s|\\.]{0,}$");

	public static Pattern COBOL_P_CALL = Pattern
			.compile("^[\\s]{1,}CALL[\\s]{1,}(?<nomeChamado>[\\S]{1,})[\\s]{1,}.*$");
	public static Pattern COBOL_P_CALL_PONTO = Pattern
			.compile("^[\\s]{1,}CALL[\\s]{1,}(?<nomeChamado>[\\S]{1,})[\\.]{1,}.*$");

	public static Pattern COBOL_P_EXEC_CICS_2 = Pattern.compile("^.*EXEC[\\s]{1,}CICS.*$");
	public static Pattern COBOL_P_EXEC_CICS_1 = Pattern
			.compile("^.*EXEC[\\s]{1,}(?<nomeChamado>CICS [\\S]{1,})[\\s]{1,}.*$");
	public static Pattern COBOL_P_END_EXEC_1 = Pattern.compile("^.*[\\s]{1,}END-EXEC[\\s]{0,}.*$");

	public static Pattern COBOL_P_EXEC_CICS_ANOTHER_1 = Pattern
			.compile("^.*EXEC[\\s]{1,}CICS[\\s]{1,}(?<parametro>[\\S]{1,})[\\s]{1,}.*$");

	public static Pattern COBOL_P_TRANSID_1 = Pattern.compile("^.*TRANSID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_CICS_FROM_1 = Pattern.compile("^.*FROM[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");

	public static Pattern COBOL_P_ENDDATA_1 = Pattern.compile("^.*ENDDATA[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_ERROR_1 = Pattern.compile("^.*ERROR[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");

	public static Pattern COBOL_P_LABEL_1 = Pattern.compile("^.*LABEL[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_QNAME_1 = Pattern.compile("^.*QNAME[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_ITEM_1 = Pattern.compile("^.* ITEM[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_NUMITEMS_1 = Pattern
			.compile("^.* NUMITEMS[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_INTO_1 = Pattern.compile("^.* INTO[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_TERMID_1 = Pattern.compile("^.*TERMID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_REQID_1 = Pattern.compile("^.*REQID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_LENGTH_1 = Pattern.compile("^.*LENGTH[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_ABSTIME_1 = Pattern.compile("^.*ABSTIME[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_TIME_1 = Pattern.compile("^.* TIME[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_QUEUE_1 = Pattern.compile("^.*QUEUE[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_RTERMID_1 = Pattern.compile("^.*RTERMID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_RTRANSID_1 = Pattern
			.compile("^.*RTRANSID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_SYSID_1 = Pattern.compile("^.*SYSID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_USERID_1 = Pattern.compile("^.*USERID[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_PROGRAM_1 = Pattern.compile("^.*PROGRAM[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");
	public static Pattern COBOL_P_HANDLE_1 = Pattern.compile("^.*HANDLE[\\s]{1,}(?<parametro>.*?)[\\s]{1,}.*$");

	public static Pattern COBOL_P_COMMAREA_1 = Pattern
			.compile("^.*COMMAREA[\\s]{0,}\\((?<parametro>.*?)[\\s]{0,}\\).*$");

	/*
	 * PATTERNS
	 */

	// public static Pattern COBOL_P_IDENTIFICATION_DIVISION = Pattern
	// .compile("^[\\.]{0,}[\\s]{1,1}IDENTIFICATION[\\s]{1,}DIVISION[\\s]{0,}[\\.]{1,1}[\\s]{0,}$");

	// | * - MODIFICACIONES EFECTUADAS - * |
	public static Pattern COBOL_P_MODIFICACAO_1_INICIO = Pattern
			.compile("^.*\\*{1,}\\s{1,}-{0,}\\s{0,}MODIFICACIONES.*$");

	// | * - MODIFICACIONES EFECTUADAS - * |
	// private final Pattern P_MODIFICACAO_2_INICIO =
	// Pattern.compile("^.*\\*{1,}\\s{0,}MODIFICACION[\\s]{0,}:.*$");

	// | * - DESCRIPCION * |
	public static Pattern COBOL_P_OBJETIVO_1_INICIO = Pattern.compile("^.*\\*{1,}\\s{1,}-{0,}\\s{1,}OBJETIVO.*$");

	// | * - DESCRIPCION : GENERACION DE RELATORIO DE OPERACIONES* |
	public static Pattern COBOL_P_OBJETIVO_2_INICIO = Pattern.compile(
			"^.*\\*{1,}\\s{1,}-{0,}\\s{0,}\\s{1,}OBJETIVO[\\s]{1,}[:]{1,}[-]{0,}(?<descricao>.*)[\\s]{0,}[\\*]{1,}.*$");

	// | * - DESCRICAO : REALIZA AS FUNCOES DE: ALTA, BAIXA, * |
	public static Pattern COBOL_P_OBJETIVO_3_INICIO = Pattern.compile(
			"^.*\\*{1,}\\s{1,}-{0,}\\s{0,}\\s{1,}OBJETIVO[\\s]{1,}[:]{1,}[-]{0,}(?<descricao>.*)[\\s]{0,}[\\*]{1,}.*$");

	// | * - ESTE PROGRAMA OBTIENE DE LA TABLA PARAMETROS LAS TABLAS * |
	public static Pattern COBOL_P_OBJETIVO_1_MEIO = Pattern
			.compile("^.*\\*{1,}\\s{0,}-{1,}(?<descricao>.*)\\s{0,}\\*{1,}.*$");

	// | * MEDIANTE TABLA DE ESTADISTICAS PARA * |
	public static Pattern COBOL_P_OBJETIVO_2_MEIO = Pattern
			.compile("^.*\\*{1,}\\s{1,}(?<descricao>.*)\\s{0,}\\*{1,}.*$");

	// | * - ESTE PROGRAMA MEDIANTE EL FICHERO DE ENTRADA FNUSESMGO |
	public static Pattern COBOL_P_OBJETIVO_3_MEIO = Pattern.compile("^.*\\*{1,}\\s{0,}-{1,}(?<descricao>.*)$");

	// | * - PROCESO DE REALIMENTACION DE LAS INCIDENCIAS PENDIENTES Y - |
	public static Pattern COBOL_P_OBJETIVO_4_MEIO = Pattern
			.compile("^.*\\*{1,}\\s{0,}-{1,}(?<descricao>.*)\\s{0,}-{1,}[\\s]{0,}$");

	// | * * |
	// private final Pattern P_OBJETIVO_1_FIM =
	// Pattern.compile("^.*\\*{1,}\\s{0,}\\*{1,}\\s{0,}$");

	// | *----------------------------------------------------------------* |
	public static Pattern COBOL_P_OBJETIVO_2_FIM = Pattern.compile("^.*\\*{1,}-{1,}\\*{1,}.*$");

	// | * -------------------------------------------------------------- |
	public static Pattern COBOL_P_OBJETIVO_3_FIM = Pattern.compile("^.*\\*{1,}[\\s]{0,}-{1,}[\\s]{0,}$");

	// | * -------------------------------------------------------------- |
	public static Pattern COBOL_P_OBJETIVO_4_FIM = Pattern.compile("^.*\\*{1,}[\\s]{1,}AUTOR[\\s]{1,}:{1,}.*$");

	// | * - DESCRIPCION * |
	public static Pattern COBOL_P_DESCRICAO_1_INICIO = Pattern.compile("^.*\\*{1,}\\s{1,}-{0,}\\s{1,}DESCRIPCION.*$");

	// | * - DESCRIPCION : GENERACION DE RELATORIO DE OPERACIONES* |
	public static Pattern COBOL_P_DESCRICAO_2_INICIO = Pattern.compile(
			"^.*\\*{1,}\\s{1,}-{0,}\\s{0,}\\s{1,}DESCRIPCION[\\s]{1,}[:]{1,}[-]{0,}(?<descricao>.*)[\\s]{0,}[\\*]{1,}.*$");

	// | * - DESCRICAO : REALIZA AS FUNCOES DE: ALTA, BAIXA, * |
	public static Pattern COBOL_P_DESCRICAO_3_INICIO = Pattern.compile(
			"^.*\\*{1,}\\s{1,}-{0,}\\s{0,}\\s{1,}DESCRICAO[\\s]{1,}[:]{1,}[-]{0,}(?<descricao>.*)[\\s]{0,}[\\*]{1,}.*$");

	/*
	 * P_VAR_SINTETICA_1 | 15 ASZGG010-SW-BLOQUES. |
	 */

	public static final Pattern COPYBOOK_P_VAR_SINTETICA_1 = Pattern
			.compile("^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_SINTETICA_1 | 01 :T:-DE-MAP EXTERNAL. | | 01 :T:-DE-MAP INDEX. |
	 */

	public static final Pattern COPYBOOK_P_VAR_SINTETICA_2 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{0,}(EXTERNAL|INDEX)[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_1 | 20 ASZGG010-SW-ORIGEN PIC X(001). |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_1 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_2 | 35 ASZGG010-DAT-CON-SDO-E OCCURS 7. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_2 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}OCCURS[\\s]{1,}(?<valorPadrao>[\\S]{1,})[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_3 | 88 ASZGG010-CF-BAJA-BLOQUEO VALUE 303. | | 88
	 * ASZGG010-CF-BAJA-BLOQUEO VALUES 303. | | 88 ASZGG010-RAZON-ANULACION VALUE
	 * 4000 THRU 4999. | | 88 ASZGG010-RAZON-ANULACION VALUES 4000 THRU 4999. |
	 * somente hierarquia 88
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_3 = Pattern.compile(
			"^[\\s]{1,}(?<hierarquia>[8]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}VALUE[sS]{0,}[\\s]{1,}(?<valorPadrao>.{1,})[\\.]{1}[\\s]{0,}$");

	/*
	 * P_VAR_ANALITICA_4 | 25 ASZGG010-VAL-CVV REDEFINES
	 * ASZGG010-VAL-CVV-CVC-CVV2-CVC2. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_4 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}REDEFINES[\\s]{1,}(?<nomeVariavelRedefinida>[\\S]{1,})[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_5 | 20 ASZGG010-CRIPT-EMV REDEFINES ASZGG010-CLAVE-PINBLOCK
	 * PIC X(074). |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_5 = Pattern
			.compile("^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}" + "(?<nomeVariavel>[\\S]{1,})[\\s]{1,}"
					+ "REDEFINES[\\s]{1,}" + "(?<nomeVariavelRedefinida>[\\S]{1,})[\\s]{1,}"
					+ "PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_6 | 05 CT-NOMBRE-PGM PIC X(009) VALUE 'LANZADERA'. | | 05
	 * CT-NOMBRE-PGM PIC X(009) VALUES 'LANZADERA'. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_6 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{1,}VALUE[S]{0,}[\\s]{1,}(?<valorPadrao>.{1,})[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_7 | 05 ATSQLERR PIC S9(9) COMP. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_7 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{1,}COMP[\\S]{0,}[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_3
	 * 
	 * | 05 CT-RUTINA-ASEMG070 VALUE 'ASEMG070'. |
	 * 
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_8 = Pattern.compile(
			"^[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}VALUE[sS]{0,}[\\s]{1,}(?<valorPadrao>.{1,})[\\.]{1}[\\s]{0,}$");

	/*
	 * 10 CODMAR PIC S9(2)V USAGE COMP-3.
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_9 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{1,}USAGE COMP-3[\\S]{0,}[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_10 | 09 EL-COMANDO OCCURS 8, INDEXED BY IND-COMANDO. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_10 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}OCCURS[\\s]{1,}(?<valorPadrao>[\\S]{1,})[\\s]{0,}[\\,]{1}.*$");

	/*
	 * P_VAR_ANALITICA_11 | 01 :T:-MESSAGE-STATUS EXTERNAL PIC S9(4) BINARY. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_11 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}EXTERNAL PIC[\\s]{1,}(?<tipo>[\\S]{1,})[\\s]{1,}BINARY[\\s]{0,}[\\.]{1}.*$");

	/*
	 * P_VAR_ANALITICA_12 | 09 EL-COMANDO OCCURS 8, INDEXED BY IND-COMANDO. |
	 */

	public static final Pattern COPYBOOK_P_VAR_ANALITICA_12 = Pattern.compile(
			"^.{0,}[\\s]{1,}(?<hierarquia>[\\d]{2,2})[\\s]{1,}(?<nomeVariavel>[\\S]{1,})[\\s]{1,}OCCURS[\\s]{1,}(?<valorPadrao>[\\S]{1,})[\\s]{1,}TIMES.*$");

	public static final Pattern COPYBOOK_P_VAR_LINHA_INICIO_1 = Pattern
			.compile("^[\\s]{1,}[\\d]{2,2}[\\s]{1,}.{0,}[\\.]{1,}[\\s]{0,}$");
	public static final Pattern COPYBOOK_P_VAR_LINHA_INICIO_2 = Pattern
			.compile("^[\\s]{1,}[\\d]{2,2}[\\s]{1,}.{0,}[^\\.]{1,}[\\s]{0,}$");
	// Pattern P_VAR_LINHA_MEIO = Pattern.compile("^.{0,}[^\\.]{1,}[\\s]{0,}$");
	public static final Pattern COPYBOOK_P_VAR_LINHA_MEIO = Pattern.compile("^[.&&[^\\.]]{1,}[\\s]{0,}$");
	public static final Pattern COPYBOOK_P_VAR_LINHA_FIM = Pattern.compile("^.{0,}[\\.]{1,}[\\s]{0,}$");
	
	
	public static Pattern COBOL_P_JOIN = Pattern.compile("[\\s]{1,}JOIN[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}");

}
