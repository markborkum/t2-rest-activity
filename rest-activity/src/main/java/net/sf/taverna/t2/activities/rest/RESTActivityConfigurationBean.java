package net.sf.taverna.t2.activities.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Beans of this class store configuration information for REST activities.
 * Configuration is comprised of the HTTP method to use, URL signature, and MIME
 * types for Accept and Content-Type HTTP request headers. Additional value is
 * used to record the format of outgoing data - binary or string. <br/>
 * <br/>
 * 
 * Also, derived attribute "activityInputs" is generated by identifying all
 * "input ports" in the provided URL signature. <br/>
 * <br/>
 * 
 * Complete request URL (obtained by substituting values into the placeholders
 * of the URL signature) is not stored, as it represents an instantiation of the
 * activity invocation. The same applies for the input message body sent along
 * with POST / PUT requests.
 * 
 * @author Sergejs Aleksejevs
 */
@SuppressWarnings("serial")
public class RESTActivityConfigurationBean implements Serializable {
	private RESTActivity.HTTP_METHOD httpMethod;
	private String urlSignature;
	private String acceptsHeaderValue;
	private String contentTypeForUpdates;
	private RESTActivity.DATA_FORMAT outgoingDataFormat;

	private boolean sendHTTPExpectRequestHeader;
	private boolean showRedirectionOutputPort;
	private boolean showActualUrlPort;
	private boolean showResponseHeadersPort;
	private Boolean escapeParameters; // whether to perform URL escaping of
										// passed parameters, true by default

	private ArrayList<ArrayList<String>> otherHTTPHeaders; // list of HTTP headers (other than Accept and Content-Type) and their values
	// only need to store the configuration of inputs, as all of them are
	// dynamic;
	// only inputs that constitute components of URL signature are to be stored
	// in this map
	// all outputs are currently fixed, so no need to keep configuration of
	// those
	private Map<String, Class<?>> activityInputs;

	/**
	 * @return An instance of the {@link RESTActivityConfigurationBean}
	 *         pre-configured with default settings for all parameters.
	 */
	public static RESTActivityConfigurationBean getDefaultInstance() {
		// TODO - set sensible default values here
		RESTActivityConfigurationBean defaultBean = new RESTActivityConfigurationBean();
		defaultBean.setHttpMethod(RESTActivity.HTTP_METHOD.GET);
		defaultBean.setAcceptsHeaderValue("application/xml");
		defaultBean.setContentTypeForUpdates("application/xml");
		defaultBean
				.setUrlSignature("http://www.uniprot.org/uniprot/{id}.xml");
		defaultBean.setOutgoingDataFormat(RESTActivity.DATA_FORMAT.String);
		defaultBean.setSendHTTPExpectRequestHeader(false); // not ticked by
															// default to allow
															// to post to
															// Twitter
		defaultBean.setShowRedirectionOutputPort(false); // not showing the
															// Redirection
															// output port by
															// default to make
															// processor look
															// simpler
		defaultBean.setShowActualUrlPort(false);
		defaultBean.setShowResponseHeadersPort(false);
		defaultBean.setEscapeParameters(true);
		defaultBean.setOtherHTTPHeaders(new ArrayList<ArrayList<String>>());
		return (defaultBean);
	}

	/**
	 * As XStream is not calling the default constructor during deserialization,
	 * we have to set the default values here for activities that did not have the 
	 * escapeParameters field set before. This method will be called by XStream
	 * after instantiating this bean.
	 */
	private Object readResolve() {
		if (escapeParameters == null){ // if this value is not set in the bean read from XML then set it to true
			escapeParameters = Boolean.TRUE;
		}
		if (otherHTTPHeaders == null){
			otherHTTPHeaders = new ArrayList<ArrayList<String>>();
		}
		return this;
	}
	
	/**
	 * Tests validity of the configuration held in this bean.
	 * 
	 * <br/>
	 * <br/>
	 * Performed tests are as follows: <br/>
	 * * <code>httpMethod</code> is known to be valid - it's an enum; <br/>
	 * * <code>urlSignature</code> - uses
	 * {@link URISignatureHandler#isValid(String)} to test validity; <br/>
	 * * <code>acceptsHeaderValue</code> and <code>contentTypeForUpdates</code>
	 * must not be empty.
	 * 
	 * <br/>
	 * <br/>
	 * <code>contentTypeForUpdates</code> is only checked if the
	 * <code>httpMethod</code> is such that it is meant to use the Content-Type
	 * header (that is POST / PUT only).
	 * 
	 * @return <code>true</code> if the configuration in the bean is valid;
	 *         <code>false</code> otherwise.
	 */
	public boolean isValid() {
		return (urlSignature != null
				&& URISignatureHandler.isValid(urlSignature)
				&& ((RESTActivity
				.hasMessageBodyInputPort(httpMethod)
				&& contentTypeForUpdates != null
				&& contentTypeForUpdates.length() > 0 && outgoingDataFormat != null) || !RESTActivity
				.hasMessageBodyInputPort(httpMethod)));
	}

	public void setHttpMethod(RESTActivity.HTTP_METHOD httpMethod) {
		this.httpMethod = httpMethod;
	}

	public RESTActivity.HTTP_METHOD getHttpMethod() {
		return httpMethod;
	}

	public String getUrlSignature() {
		return urlSignature;
	}

	public void setUrlSignature(String urlSignature) {
		this.urlSignature = urlSignature;
	}

	public String getAcceptsHeaderValue() {
		return acceptsHeaderValue;
	}

	public void setAcceptsHeaderValue(String acceptsHeaderValue) {
		this.acceptsHeaderValue = acceptsHeaderValue;
	}

	public String getContentTypeForUpdates() {
		return contentTypeForUpdates;
	}

	public void setContentTypeForUpdates(String contentTypeForUpdates) {
		this.contentTypeForUpdates = contentTypeForUpdates;
	}

	public void setActivityInputs(Map<String, Class<?>> activityInputs) {
		this.activityInputs = activityInputs;
	}

	public Map<String, Class<?>> getActivityInputs() {
		return activityInputs;
	}

	public RESTActivity.DATA_FORMAT getOutgoingDataFormat() {
		return outgoingDataFormat;
	}

	public void setOutgoingDataFormat(
			RESTActivity.DATA_FORMAT outgoingDataFormat) {
		this.outgoingDataFormat = outgoingDataFormat;
	}

	public boolean getSendHTTPExpectRequestHeader() {
		return sendHTTPExpectRequestHeader;
	}

	public void setSendHTTPExpectRequestHeader(
			boolean sendHTTPExpectRequestHeader) {
		this.sendHTTPExpectRequestHeader = sendHTTPExpectRequestHeader;
	}

	public boolean getShowRedirectionOutputPort() {
		return showRedirectionOutputPort;
	}

	public void setShowRedirectionOutputPort(boolean showRedirectionOutputPort) {
		this.showRedirectionOutputPort = showRedirectionOutputPort;
	}

	public void setEscapeParameters(boolean escapeParameters) {
		this.escapeParameters = Boolean.valueOf(escapeParameters);
	}
	
	public boolean getEscapeParameters() {
		return this.escapeParameters.booleanValue();
	}

	public void setOtherHTTPHeaders(ArrayList<ArrayList<String>> otherHTTPHeaders) {
		this.otherHTTPHeaders = otherHTTPHeaders;
	}

	public ArrayList<ArrayList<String>> getOtherHTTPHeaders() {
		return otherHTTPHeaders;
	}

	/**
	 * @return the showActualUrlPort
	 */
	public boolean getShowActualUrlPort() {
		return showActualUrlPort;
	}

	/**
	 * @param showActualUrlPort the showActualUrlPort to set
	 */
	public void setShowActualUrlPort(boolean showActualUrlPort) {
		this.showActualUrlPort = showActualUrlPort;
	}

	/**
	 * @return the showResponseHeadersPort
	 */
	public boolean getShowResponseHeadersPort() {
		return showResponseHeadersPort;
	}

	/**
	 * @param showResponseHeadersPort the showResponseHeadersPort to set
	 */
	public void setShowResponseHeadersPort(boolean showResponseHeadersPort) {
		this.showResponseHeadersPort = showResponseHeadersPort;
	}
	
}
