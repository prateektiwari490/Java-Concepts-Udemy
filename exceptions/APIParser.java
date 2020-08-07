package exceptions;

public class APIParser {
	public static int parseSendResponseCode(String response, String data, String partner) throws APIFormatChangeException {
		int responseCode = -1;
		try {
			String startTag = "<code>";
			String endTag = "</code>";
			if (response.contains(startTag)) {
				int beginIndex = response.indexOf(startTag) + startTag.length();
				int endIndex = response.indexOf(endTag);
				System.out.println("code: " + response.substring(beginIndex, endIndex));
				responseCode = Integer.parseInt(response.substring(beginIndex, endIndex));
			}
		} catch (NumberFormatException e) {
			//throw new APIFormatChangeException("Response: " + response + ", Element: code, Partner: " + partner);
			APIFormatChangeException e1 = new APIFormatChangeException(response, "code", partner);
			e1.initCause(e);
			throw e1;
		}
		
		return responseCode;
	}
}
