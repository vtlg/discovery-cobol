package br.gov.caixa.discovery.core.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

public class UtilsHandler {

	public static final String MD2 = "MD2";
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	public static final String SHA224 = "SHA-224";
	public static final String SHA256 = "SHA-256";
	public static final String SHA384 = "SHA-384";
	public static final String SHA512 = "SHA-512";

	public static String calcularHash(String valorEntrada, String method) {
		return calcularHash(valorEntrada, method, "UTF-8");
	}

	public static String calcularHash(String valorEntrada, String method, String encode) {
		String valorSaida = null;
		try {
			valorSaida = DatatypeConverter
					.printHexBinary(MessageDigest.getInstance(method).digest(valorEntrada.getBytes(encode)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return valorSaida;
	}

	public static String joinArray(Object[] array, char separator) {
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					sb.append("");
				} else {
					sb.append(array[i]);
				}

				if (i < (array.length - 1)) {
					sb.append(separator);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String joinList(Collection<String> valorEntrada, char separator) {
		return joinArray(valorEntrada.toArray(new String[0]), separator);
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	public static String removerPontuacao(String texto) {
		String tempTexto = texto;

		tempTexto = texto.replaceAll("[\\.\\,]", "");

		return tempTexto;
	}

	public static String removerPontoFinal(String texto) {
		String tempTexto = texto;
		if (tempTexto.substring(tempTexto.length() - 1).equals(".")) {
			tempTexto = tempTexto.substring(0, tempTexto.length() - 1);
		}
		return tempTexto;
	}

	public static String converter2String(Object entry) {
		return converter2String(entry, false);
	}

	public static String converter2String(Object entry, boolean upperCase) {
		if (entry == null) {
			return null;
		}

		if (entry instanceof Boolean) {
			if ((Boolean) entry) {
				return "TRUE";
			} else {
				return "FALSE";
			}
		}

		if (entry instanceof BigInteger) {
			return ((BigInteger) entry).toString();
		}

		return entry.toString();
	}

	public static Long converter2Long(Object entry) {
		if (entry == null) {
			return null;
		}

		if (entry instanceof BigInteger) {
			return ((BigInteger) entry).longValue();
		}
		if (entry instanceof String) {
			return Long.parseLong(((String) entry));
		}

		return Long.parseLong(((String) entry));
	}

	public static Integer converter2Integer(Object entry) {
		if (entry == null) {
			return null;
		}

		if (entry instanceof Integer) {
			return (Integer) entry;
		}
		if (entry instanceof String) {
			return Integer.parseInt(((String) entry));
		}

		return Integer.parseInt(((String) entry));
	}

	public static Calendar converter2Calendar(Object entry) {
		if (entry == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();

		if (entry instanceof Timestamp) {
			cal.setTimeInMillis(((Timestamp) entry).getTime());
			return cal;
		}

		return null;
	}

	public static Boolean converter2Boolean(Object entry) {
		if (entry == null) {
			return null;
		}

		if (entry instanceof Boolean) {
			return Boolean.parseBoolean(entry.toString());
		}

		return null;
	}

	public static byte[] converter2ArrayByte(Object entry) {
		if (entry == null) {
			return null;
		}

		if (entry instanceof byte[]) {
			return (byte[]) entry;
		}

		return null;
	}

	public static List<Path> recuperarListaArquivo(String pasta, boolean verificarSubpastas) {
		List<Path> listaPasta = null;
		List<Path> listaOutput = new ArrayList<>();

		try {
			listaPasta = Files.list(Paths.get(pasta)).collect(Collectors.toList());

			listaPasta.stream().forEach((path) -> {
				if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(path)) {
					if (!path.getFileName().toString().endsWith(".jasper")
							&& !path.getFileName().toString().endsWith(".png")
							&& !path.getFileName().toString().endsWith(".class")
							&& !path.getFileName().toString().endsWith(".jrxml")
							&& !path.getFileName().toString().endsWith(".xls")
							&& !path.getFileName().toString().endsWith(".docx")) {
						listaOutput.add(path);
					}
				} else if (verificarSubpastas && !Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)
						&& Files.isReadable(path)) {
					List<Path> listaTemp = recuperarListaArquivo(path.toString(), true);
					if (listaTemp != null) {
						listaOutput.addAll(listaTemp);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listaOutput;
	}

}
