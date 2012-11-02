package com.tscp.mvno.webservices;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.spcs.pls.m2m.AccessEqpAsgmInfo;
import com.spcs.pls.m2m.AccessNbrAsgmInfo;
import com.spcs.pls.m2m.ActivateReserveSubscription;
import com.spcs.pls.m2m.ActivateReserveSubscriptionHolder;
import com.spcs.pls.m2m.ActivateReservedSubscriptionIn;
import com.spcs.pls.m2m.ActivateReservedSubscriptionOut;
import com.spcs.pls.m2m.ActivateReservedSubscriptionOutHolder;
import com.spcs.pls.m2m.CancelPortInformation;
import com.spcs.pls.m2m.CancelPortResult;
import com.spcs.pls.m2m.CancelPortResultHolder;
import com.spcs.pls.m2m.ChangePortInDueDateAndTimeInformation;
import com.spcs.pls.m2m.ChangePortInDueDateAndTimeResult;
import com.spcs.pls.m2m.ChangePortInDueDateAndTimeResultHolder;
import com.spcs.pls.m2m.CsaListHolder;
import com.spcs.pls.m2m.ModifyPortInformation;
import com.spcs.pls.m2m.ModifyPortResult;
import com.spcs.pls.m2m.ModifyPortResultHolder;
import com.spcs.pls.m2m.NPA;
import com.spcs.pls.m2m.PLSSali;
import com.spcs.pls.m2m.PLSSaliListHolder;
import com.spcs.pls.m2m.PendingSubscription;
import com.spcs.pls.m2m.PendingSubscriptionHolder;
import com.spcs.pls.m2m.PendingSubscriptionNPA;
import com.spcs.pls.m2m.PendingSubscriptionNPAHolder;
import com.spcs.pls.m2m.PortInformation;
import com.spcs.pls.m2m.PortResult;
import com.spcs.pls.m2m.PortResultHolder;
import com.spcs.pls.m2m.Sali;
import com.spcs.pls.m2m.Sali2;
import com.spcs.pls.m2m.Sali2Holder;
import com.spcs.pls.m2m.SaliList2Holder;
import com.spcs.pls.m2m.ServiceSub;
import com.spcs.pls.m2m.ServiceSub2;
import com.spcs.pls.m2m.SocProfileListHolder;
import com.spcs.pls.m2m.Subscription;
import com.spcs.pls.m2m.SubscriptionHolder;
import com.spcs.pls.m2m.SubscriptionNPA;
import com.spcs.pls.m2m.SubscriptionNPAHolder;
import com.spcs.pls.m2m.ValidNPA;
import com.spcs.pls.m2m.ValidNPAHolder;
import com.spcs.pls.m2m.general_application_error_exception;
import com.spcs.pls.m2m.invalid_csa_exception;
import com.spcs.pls.m2m.invalid_date_exception;
import com.spcs.pls.m2m.invalid_esn_exception;
import com.spcs.pls.m2m.invalid_mdn_exception;
import com.spcs.pls.m2m.invalid_time_zone_exception;
import com.spcs.pls.m2m.invalid_value_exception;
import com.spcs.pls.m2m.logout_failed_exception;
import com.spcs.pls.m2m.missing_field_exception;
import com.tscp.mvno.api.APIActivateReserveSubscriptionResponseHolder;
import com.tscp.mvno.api.APICSAListResponseHolder;
import com.tscp.mvno.api.APIChangePortInDueDateAndTimeResultResponseHolder;
import com.tscp.mvno.api.APICsaResponseHolder;
import com.tscp.mvno.api.APIGeneralResponseHolder;
import com.tscp.mvno.api.APIGetReserveSubscriptionResponseHolder;
import com.tscp.mvno.api.APIModifyPortResultResponseHolder;
import com.tscp.mvno.api.APIPendingSubscriptionNPAResponseHolder;
import com.tscp.mvno.api.APIPortCancelResponseHolder;
import com.tscp.mvno.api.APIPortInActivateSubscriptionResponseHolder;
import com.tscp.mvno.api.APIPortInSwapMDNResponseHolder;
import com.tscp.mvno.api.APIPortResultResponseHolder;
import com.tscp.mvno.api.APIResellerReservedSubscriptionResponseHolder;
import com.tscp.mvno.api.APIResellerSubInquiryResponseHolder;
import com.tscp.mvno.api.APIResellerUsageInquiryRepsonseHolder;
import com.tscp.mvno.api.APIReserveSubscriptionResponseHolder;
import com.tscp.mvno.api.APISocListResponseHolder;
import com.tscp.mvno.api.APISplitNpaMDNResponseHolder;
import com.tscp.mvno.api.APISubscriptionNPAResponseHolder;
import com.tscp.mvno.api.APISubscriptionResponseHolder;
import com.tscp.mvno.api.APISwapESNResponseHolder;
import com.tscp.mvno.api.APISwapMDNResponseHolder;
import com.tscp.mvno.api.APIValidNPAListResponseHolder;
import com.tscp.mvno.api.APIactivateSubscriptionMSIDHolder;
import com.tscp.mvno.dbresource.CreateDBRequestEntry;
import com.tscp.mvno.exception.SprintAPIException;
import com.tscp.mvno.helper.APIConversionUtil;
import com.tscp.mvno.helper.APIHelper;
import com.tscp.mvno.helper.APIInit;
import com.tscp.mvno.helper.APIValidationUtil;
import com.tscp.mvno.session.APIKey;
import com.tscp.mvno.session.APISessionHelper;
import com.tscp.mvno.util.db.SPArgs;
import com.tscp.mvno.util.db.StoredProc;
import com.tscp.mvno.util.logger.DCILogger;




/**
 * @author CCampos
 *
 */

@WebService
public class API3 {
	
	// [start] members
	
    //config params
	private String  mRoot = System.getProperty("serverroot");
	//private String 	mConfigFile = "/apps/webadmin/SUNWappserver/domains/mvnoprod/config/api.config";
	
	private APIConversionUtil apiConversionUtil; 
	private boolean webServiceConnection;
	private boolean validateOutboundRequest;
	private String mUserName;
	private String mOrderID;
	
	/**
	 *
	 *     SPECIAL NOTE:  
	 *     private boolean activeSprintConnection = false; (WHEN API3.class IS DEPLOYED TO TEST SERVER)
	 *     private boolean activeSprintConnection = true; (WHEN API3.class IS DEPLOYED TO PRODUCTION SERVER)
	 * 
	 * */
	
	private String mConfigFile = System.getProperty("apiconfig");
    private Properties p = null;
    private boolean debugMode = true;
    private boolean activeSprintConnection = true;
    private InitialContext initialcontext = null ;
    private String k11DbName = "jdbc/K11MVNODB";
    private DataSource k11DataSource = null;
        
    private DCILogger logger;
    public static final String LOG_EVENT_TYPE = APIHelper.LOG_EVENT_TYPE; //"event";
    public static final String LOG_INFO       = APIHelper.LOG_INFO;       // "info";
    public static final String LOG_WARNING    = APIHelper.LOG_WARNING;    //"warn";
    public static final String LOG_ERROR      = APIHelper.LOG_ERROR;      //"error";
      
    private CreateDBRequestEntry createDBRequestEntry = null;
    
    public static final String RESTORE_SVC_CODE = "PRSALL022";
    public static final String SUSPEND_SVC_CODE = "PRSALL023";
        
        //Key or Session params 
    private APISessionHelper aPISessionHelper = null;
  	private static com.spcs.pls.m2m.M2mSubMaintenance6 subMaintenance6 = null;
	private com.spcs.pls.m2m.ResellerV2SubInquiry resellerV2SubInquiry = null;
	private com.spcs.pls.m2m.ResellerV2UsageInquiry resellerV2UsageInquiry = null;
	
	private String activity;
    private org.omg.CORBA.StringHolder confirmMsg;
	
	//will be used to return back some status and error message if any to the caller
	private String responseStr = "";
	private String statusStr   = "SUCCEED";
	
	private ValidNPA validNPA;
	private ValidNPAHolder validNPAHolder; 
	
	private SubscriptionNPAHolder subscriptionNPAHolder;
	
	
	private PortResult portResult;
	private PortResultHolder portResultHolder;
	
	private SubscriptionHolder subHolder;
	private APISubscriptionNPAResponseHolder aPISubscriptionNPAResponseHolder;
	
	private ModifyPortResult modifyPortResult;
	private ModifyPortResultHolder modifyPortResultHolder;
		
	private ChangePortInDueDateAndTimeResult changePortInDueDateAndTimeResult;
	private ChangePortInDueDateAndTimeResultHolder changePortInDueDateAndTimeResultHolder;
		
	//Response Wrapper for APIprePortValidation method: PortResult Object 
	private APIPortResultResponseHolder aPIPortResultResponseHolder;
	
	//Response Wrapper for ValidNPAList Object
	private APIValidNPAListResponseHolder aPIValidNPAListResponseHolder;
	//Reponse Wrapper for Subscription Object
	private APISubscriptionResponseHolder aPISubscriptionResponseHolder;
	
	//
	private APIModifyPortResultResponseHolder aPIModifyPortResultResponseHolder;
	
	//
	private APIGeneralResponseHolder aPIGeneralResponseHolder;
	
	//
	private APIChangePortInDueDateAndTimeResultResponseHolder aPIChangePortInDueDateAndTimeResultResponseHolder;
	
	//Use for getCSA()
	private APICsaResponseHolder aPICsaResponseHolder;
	private String CSA = "";
	
	private PendingSubscriptionNPAHolder pendingSubscriptionNPAHolder;
	
	//
	private APIPendingSubscriptionNPAResponseHolder aPIPendingSubscriptionNPAResponseHolder;
	
	
	//
	private ActivateReserveSubscriptionHolder activateReserveSubscriptionHolder;
	private APIActivateReserveSubscriptionResponseHolder aPIActivateReserveSubscriptionResponseHolder;
	
	
	//getCSAList
	private CsaListHolder csaListHolder;
	private APICSAListResponseHolder aPICSAListResponseHolder;
	
	//swapMDN
	private APISwapMDNResponseHolder aPISwapMDNResponseHolder;
	//swapESN
	private APISwapESNResponseHolder aPISwapESNResponseHolder;
	
	//used for both swapMDN and swapESN
	private org.omg.CORBA.StringHolder newMDN;
	private org.omg.CORBA.StringHolder MSID;
	
	//1.1.24	portInSwapMDN()
	private APIPortInSwapMDNResponseHolder aPIPortInSwapMDNResponseHolder;
	
	//getPpSocList()
	private SocProfileListHolder socProfileListHolder;
	private APISocListResponseHolder aPISocListResponseHolder;
	
	//reserveSubscription()
	private APIReserveSubscriptionResponseHolder aPIReserveSubscriptionResponseHolder;
	private PendingSubscriptionHolder pendingSubscriptionHolder;
	
	
	//getReservedSubscription()
	private APIGetReserveSubscriptionResponseHolder aPIGetReserveSubscriptionResponseHolder;
    private org.omg.CORBA.StringHolder MDN;
	private org.omg.CORBA.StringHolder commSvcAreaId; 
	private org.omg.CORBA.StringHolder majorAcctNbr; 
	private Sali2Holder pricePlan; 
	private org.omg.CORBA.StringHolder reserveMDNID; 
	private org.omg.CORBA.StringHolder rsvDt; 
	private org.omg.CORBA.StringHolder rsvSbscrpId;
	private org.omg.CORBA.StringHolder rsvTm; 
	private SaliList2Holder serviceList;
	
    private APIInit apiInit;	
	
	// SplitNpaMDN
	APISplitNpaMDNResponseHolder aPISplitNpaMDNResponseHolder = null;
	
	// [end] members
	
	/**
	 * 
	 * API Constructor -- Default Constructor that is empty
	 * 
	 *     SPECIAL NOTE:  
	 *     activeSprintConnection = false; (WHEN API3.class IS DEPLOYED TO TEST SERVER)
	 *     activeSprintConnection = true; (WHEN API3.class IS DEPLOYED TO PRODUCTION SERVER)
	 * 
	 */
	public API3(){
		apiConversionUtil = new APIConversionUtil();
		activeSprintConnection = true;
		webServiceConnection = true;
		validateOutboundRequest = true;
		mUserName = "tscp";
		mOrderID = "0000";

	}
     
	@WebMethod
	public String getConfigPath() { return mConfigFile; }
	
	@WebMethod
	public void setConfigPath(String idx) { mConfigFile = idx; }
	
	@WebMethod
	public String setActiveSprintConnection( String trueFalse ) {
		String retValue = "";
		if( trueFalse != null ) {
			if( trueFalse.equalsIgnoreCase("false") ) {
				activeSprintConnection = false;
				retValue = "Active Sprint Connection has been updated to "+trueFalse;
				logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** activeSprintConnection is set to false ****");
			} else if( trueFalse.equalsIgnoreCase("true") ) {
				activeSprintConnection = true;
				retValue = "Active Sprint Connection has been updated to "+trueFalse;
				logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** activeSprintConnection is setd to true ****");
			} else {
				retValue = "Unknown entry::" + trueFalse + ". Please specifiy either true or false";
			}
		} else 
			retValue = "No change has occurred ... Sprint Active Connection is still: " + activeSprintConnection;
		return retValue;
	}
	
	@WebMethod
	public String setSprintWebServiceConnection( String trueFalse ) {
		String retValue = "";
		if( webServiceConnection && trueFalse.equalsIgnoreCase("false") ) {
			if( apiInit == null ) {
				init();
			} else {
				apiInit.init(true);
		        subMaintenance6  = apiInit.getOrbInterface();
		        aPISessionHelper = apiInit.getSessionHelper();
		        resellerV2SubInquiry = apiInit.getResellerV2SubInquiry();
		        resellerV2UsageInquiry = apiInit.getResellerV2UsageInquiry();
				if( resellerV2SubInquiry == null ) {
					System.out.println("Initializing Corba utilities...");
					initAll();
				}
			}
		}
		
		if( trueFalse != null ) {
			if( trueFalse.equalsIgnoreCase("false") ) {
				webServiceConnection = false;
				retValue = "Sprint WS Connection has been updated to "+trueFalse;
				logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** webServiceConnection is set to [ "+webServiceConnection+" ] ****");
			} else if( trueFalse.equalsIgnoreCase("true") ) {
				webServiceConnection = true;
				retValue = "Sprint WS Connection has been updated to "+trueFalse;
				logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** webServiceConnection is set to [ "+webServiceConnection+" ] ****");
			} else {
				retValue = "Unknown entry::" + trueFalse + ". Please specifiy either true or false";
			}
		} else 
			retValue = "No change has occurred ... Sprint WS Connection is still: " + webServiceConnection;
		
		return retValue;
	}
	
	@WebMethod
	public String setOutboundValidation( String trueFalse ) {
		String retValue = "";
		if( trueFalse != null ) {
			if( trueFalse.equalsIgnoreCase("false") ) {
				validateOutboundRequest = false;
				retValue = "Outbound Validation flag has been updated to "+trueFalse;
			} else if( trueFalse.equalsIgnoreCase("true") ) {
				validateOutboundRequest = true;
				retValue = "Outbound Validation flag has been updated to "+trueFalse;
			} else {
				retValue = "Unknown entry::"+trueFalse+". Please specifiy either true or false";
			}
		} else 
			retValue = "No change has occurred ... Outbound Validation flag is still: " + webServiceConnection;
		return retValue;
	}

	@WebMethod
	public String setDebugMode( String trueFalse ) {
		String retValue = "";
		if( trueFalse != null ) {
			if( trueFalse.equalsIgnoreCase("false") ) {
				debugMode = false;
				retValue = "Debug Mode has been updated to "+trueFalse;
			} else if( trueFalse.equalsIgnoreCase("true") ) {
				debugMode = true;
				retValue = "Debug Mode has been updated to "+trueFalse;
			} else {
				retValue = "Unknown entry::"+trueFalse+". Please specifiy either true or false";
			}
		} else 
			retValue = "No change has occurred";
		return retValue;
	}
	
	@WebMethod 
	public String getRootDir() 
	{ 
		return mRoot; 
	} 
	
	public boolean getActiveSprintConnection(){
		return activeSprintConnection;
	}
	
	public boolean getWebServiceConnection(){
		return webServiceConnection;
	}
	
	/**
	 * Takes the local instance of apiInit and binds it to a new instance of the object\
	 * @return
	 */
    protected boolean initAll()
    {
    	apiInit = new APIInit();
      //apiInit = new APIInit(mConfigFile);
    	//apiInit = APIInit.instance();
    	if( webServiceConnection ) {
    		if( apiInit.init(!webServiceConnection) ) {
    			p			= apiInit.getProperties();
    			logger		= apiInit.getLogger();    			
    		}
    	} else {
	      if(apiInit.init())
	      {
	        p                = apiInit.getProperties();
	        logger           = apiInit.getLogger();
	        subMaintenance6  = apiInit.getOrbInterface();
	        aPISessionHelper = apiInit.getSessionHelper();
	        resellerV2SubInquiry = apiInit.getResellerV2SubInquiry();
	        resellerV2UsageInquiry = apiInit.getResellerV2UsageInquiry();
	 
	      }
    	}
	  
      logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** activeSprintConnection is initalized to ["+activeSprintConnection+"] ****");
	  logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** webServiceConnection is initalized to ["+webServiceConnection+"] ****");
      return apiInit.getInitStatus();
    }


        /**
        * Will simply reset the status message and 
        */ 
        private void resetStatusMessage()
        {
           responseStr = "";
           statusStr   = "SUCCEED";
        }
        
        
        @WebMethod 
        public void APIreleaseSessions(){
        	/*
           if(aPISessionHelper != null){
            aPISessionHelper.releaseSessions();
           }else{
             logger.log(APIHelper.LOG_EVENT_TYPE, APIHelper.LOG_WARNING, "Sessions have either been released or have not been initialized");
           }*/
        	System.out.println("ROOT :: " + mRoot);
        }
	
    /**
     * init() is designed to grant access to methods to individually instantiate the logger object. 
     * <p>
     * 	previously this logical block was part of the APILogin functionality however succeesful login should not dictate the instantiation of
     * 	 the logger.
     * </p>
     * 
     * @return
     */
    @WebMethod
    public boolean init() {
        if( apiInit != null ){
           if(!apiInit.getInitStatus()){
             initAll();                      //initialize all interface
           } 
        }else{
          initAll();                      //initialize all interface
        }
        return true;
    }
        
	/**
	 * This method can only be used when an MDN is reserved via the 
	 *       reserveSubscription() method
	 * 
	 * @param activateReserveSubscription record of  {@link com.spcs.pls.m2m.ActivateReserveSubscription} structure. (required)
	 * <table border>
	 *     <th>name</th><th>object_type</th><th>usage</th>
	 *     <tr><td>MDN</td><td>String</td><td><b>required</td></tr>
	 *     <tr><td>ESN</td><td>String</td><td><b>required</td></tr>
	 *     <tr><td>MSID</td><td>String</td><td>not used</td></tr>
	 *     <tr><td>effectiveDate</td><td>String</td><td><i>optional</td></tr>
	 * </table>
	 * 
	 * @return APIActivateReserveSubscriptionResponseHolder
	 * <table border>
	 *     <th>name</th><th>object_type</th>
	 *     <tr><td>MDN</td><td>String</td></tr>
	 *     <tr><td>ESN</td><td>String</td></tr>
	 *     <tr><td>MSID</td><td>String</td></tr>
	 *     <tr><td>effectiveDate</td><td>String</td></tr>
	 * </table>
	 *  
	 */
        @WebMethod
	public APIActivateReserveSubscriptionResponseHolder APIactivatePendingSubscription(ActivateReserveSubscription  activateReserveSubscription )
	{
        init();
        resetStatusMessage();
    	String lApiName = "activatePendingSubscription";
        activateReserveSubscriptionHolder = new ActivateReserveSubscriptionHolder(activateReserveSubscription);
		createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIactivatePendingSubscription ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** activateReserveSubscription MDN    :: "+activateReserveSubscription.MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** activateReserveSubscription ESN    :: "+activateReserveSubscription.ESN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** activateReserveSubscription MSID   :: "+activateReserveSubscription.MSID+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** activateReserveSubscription EffDate:: "+activateReserveSubscription.effectiveDate+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
                APIKey apikey  = APIlogin();
                String key = apikey.getKeyValue();
                
		    	try{	
		    		subMaintenance6.activatePendingSubscription(key, activateReserveSubscriptionHolder);
	                
				}catch(invalid_esn_exception esn_ex){
					responseStr =  buildStackTraceErrorResponse(esn_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
				}catch(invalid_mdn_exception mdn_ex){
					responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
				}catch(missing_field_exception field_ex){
					responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
				}catch(general_application_error_exception gen_ex){
					responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
				}catch(Exception e){
				     responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			  	     statusStr ="FAIL";
	                             
				}finally{
					try{
					   createDBRequestEntry = new CreateDBRequestEntry(logger);
					   createDBRequestEntry.logDBRequest(activateReserveSubscription);
					}catch(Exception e){
					   responseStr = buildStackTraceErrorResponse(e.getStackTrace());
					}
	
					aPIActivateReserveSubscriptionResponseHolder =
						new APIActivateReserveSubscriptionResponseHolder(
								      activateReserveSubscriptionHolder.value,
								      responseStr,
								      statusStr);
	                           APIlogout(apikey);
				}
        	} else { // use Sprint's web service
        		try{
        			if( validateOutboundRequest ) {
        				APIValidationUtil validationUtil = new APIValidationUtil(logger);
        				APIActivateReserveSubscriptionResponseHolder validatedResponse = validationUtil.validateActivatePendingSubscriptionRequest(mUserName, mOrderID, createDBRequestEntry, activateReserveSubscription);
        				if( validatedResponse != null ) {
        					logger.log(LOG_EVENT_TYPE, LOG_INFO, "API Validation completed with the following message: "+validatedResponse.responseMessage);
        					if( validatedResponse.statusMessage.equals(APIValidationUtil.RESPONSE_STATUS_SUCCEED) ) {
        						aPIActivateReserveSubscriptionResponseHolder = validatedResponse;
        					} else {
        						aPIActivateReserveSubscriptionResponseHolder = apiConversionUtil.APIactivatePendingSubscription(mUserName, mOrderID, activateReserveSubscription);
        					}
        				} else {
        					aPIActivateReserveSubscriptionResponseHolder = apiConversionUtil.APIactivatePendingSubscription(mUserName, mOrderID, activateReserveSubscription);
        				}
        			} else {
        				aPIActivateReserveSubscriptionResponseHolder = apiConversionUtil.APIactivatePendingSubscription(mUserName, mOrderID, activateReserveSubscription);
        			}
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIActivateReserveSubscriptionResponseHolder = new APIActivateReserveSubscriptionResponseHolder(null, responseStr, api_ex.getStatus());        			
        		}
        	}
        } else {
            activateReserveSubscriptionHolder = new ActivateReserveSubscriptionHolder(activateReserveSubscription);
        	aPIActivateReserveSubscriptionResponseHolder = 
        		new APIActivateReserveSubscriptionResponseHolder(
				      activateReserveSubscriptionHolder.value,
				      "Sprint Activation Bypassed",
				      "SUCCEED");
        }
		if( debugMode ) {
			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIactivatePendingSubscription ****");
		}
		createDBRequestEntry = null;
		return aPIActivateReserveSubscriptionResponseHolder;
	}

    
	//----------------------------------------------------------------------
	/**
     * This API is used to add a new subscription.
     * 
     * @param sub  {@link com.spcs.pls.m2m.Subscription} Object representing a pending Subscription (required)
     * <table border>
     *     <th>object_name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     	   <td>activity</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     *     <tr>
     *     	   <td>MDN</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     *     <tr>
     *     	   <td>ESN</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>CSA</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>pricePlans</td>
     *         <td>{@link com.spcs.pls.m2m.Sali}</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>services</td>
     *         <td>{@link com.spcs.pls.m2m.Sali}[]</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     * </table>
     * @return APISubscriptionResponseHolder
     */
    @WebMethod 
    public APISubscriptionResponseHolder APIactivateSubscription(Subscription sub)
    {
    	String lApiName = "activateSubscription";
    	resetStatusMessage();
    	APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();
    	subHolder =new SubscriptionHolder(sub);
    	try{
            createDBRequestEntry = new CreateDBRequestEntry(logger);
    	    subMaintenance6.activateSubscription(key, subHolder);
    	}catch(invalid_esn_exception esn_ex){
    		responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
    	}catch(invalid_date_exception date_ex){
    		responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
    	}catch(invalid_mdn_exception mdn_ex){
    		responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
    	}catch(missing_field_exception field_ex){
    		responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
    	}catch(general_application_error_exception gen_ex){
    		responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
    	}catch(Exception e){
    		 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
      	     statusStr ="FAIL";
		}finally{
			aPISubscriptionResponseHolder = new APISubscriptionResponseHolder(subHolder.value,responseStr,statusStr);	
                           APIlogout(apikey);
		}
    	
    	return aPISubscriptionResponseHolder;
    }
//--------------------------------------------------------------------------------------
    /**
     * Method will be used to activate a subscription using the MSID  
     * 
     * @param subIn {@link com.spcs.pls.m2m.ActivateReservedSubscriptionIn} 
     * <table border>
     *     <th>name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     		<td>activity</td><td>String</td><td><i>optional</td>
     *     </tr>
     * </table>
     * @return APISubscriptionNPAResponseHolder
     * 
     */
    @WebMethod
    public APIactivateSubscriptionMSIDHolder APIactivateSubscriptionMSID(ActivateReservedSubscriptionIn subIn)
    {
        String lApiName = "activateSubscriptionMSID";
        resetStatusMessage();
        APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();
        ActivateReservedSubscriptionOutHolder subOutholder = null; 
        APIactivateSubscriptionMSIDHolder holder = null;
        try{
        	createDBRequestEntry = new CreateDBRequestEntry(logger);
            subOutholder = new ActivateReservedSubscriptionOutHolder(new ActivateReservedSubscriptionOut()); 
            subMaintenance6.activateSubscriptionMSID(key, subIn, subOutholder);
    	}catch(invalid_esn_exception esn_ex){
    		responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
    	}catch(invalid_date_exception date_ex){
    		responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
    	}catch(invalid_mdn_exception mdn_ex){
    		responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
    	}catch(missing_field_exception field_ex){
    		responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
    	}catch(general_application_error_exception gen_ex){
    		responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
        }catch(Exception e){
            responseStr = buildStackTraceErrorResponse(e.getStackTrace());
            statusStr ="FAIL";
        }finally{
            holder = new APIactivateSubscriptionMSIDHolder(subOutholder.value, responseStr, statusStr);
        }
  
     return holder;
    }	
 //--------------------------------------------------------------------------------------

    /**
     * Method should only be used when activating a subscription using an NPA. 
     *        If an MDN is present, activateSubscription should be used.
     *        
     * @param subNPA {@link com.spcs.pls.m2m.SubscriptionNPA} 
     * @return APISubscriptionNPAResponseHolder
     *  
     */
    @WebMethod
    public APISubscriptionNPAResponseHolder APIactivateSubscriptionNPA(SubscriptionNPA subNPA)
    {
    	String lApiName = "activateSubscriptionNPA";
        resetStatusMessage();
        APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();
    	subscriptionNPAHolder = new SubscriptionNPAHolder(subNPA);
    	
    	
    	
    	try{
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIactivateSubscriptionNPA ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA Activity    :: "+subNPA.activity+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA CSA         :: "+subNPA.CSA+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA NPA         :: "+subNPA.npa+" **** ****");
            Sali lSali = subNPA.pricePlans;
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA svcCode     :: "+lSali.svcCode+" **** ****");
            Sali[] prodList = subNPA.services;
            if( prodList != null && prodList.length > 0 ) {
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Service List **** ****");
	            for( int i = 0; i < prodList.length; i++ ) {
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcCode::"+prodList[i].svcCode+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcEffDt::"+prodList[i].svcEffDt+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcExpDt::"+prodList[i].svcExpDt+" **** **** ****");
	            }
	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** Done Service List **** ****");
            }
            createDBRequestEntry = new CreateDBRequestEntry(logger);
    	subMaintenance6.activateSubscriptionNPA(key, subscriptionNPAHolder);
    	}catch(invalid_date_exception date_ex){
    		responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
        }catch(invalid_esn_exception esn_ex){
        	responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
        }catch(invalid_mdn_exception mdn_ex){
        	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
        }catch(general_application_error_exception gen_ex){
        	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
        }catch(missing_field_exception field_ex){
        	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
        }catch(Exception e){
        	 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
      	     statusStr ="FAIL";
        }finally{
        	aPISubscriptionNPAResponseHolder =
        		new APISubscriptionNPAResponseHolder(subscriptionNPAHolder.value, 
        				                             responseStr, 
        				                             statusStr);
                APIlogout(apikey);
        	
        }
        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIactivateSubscriptionNPA ****");
        
        return aPISubscriptionNPAResponseHolder;
    }

 //--------------------------------------------------------------------------------------
    
	
 //---------------------------------------------------------------------------------------	
	
    /**
     * This API is used to initiate a pre-port validation of a Reseller access number. 
     * 
     * @param String key - session
     * @param activity - Y = Hot Phone, N will not be used for Hot Phone defaul is N
     * @param portInfo {@link com.spcs.pls.m2m.PortInformation}
     * <table border> 
     *     <th>object_name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     	   <td>mdn</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>timeZone</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>authorizedBy</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billFirstName</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billLastName</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>businessName</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetNumber</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetName</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetDirectionIndicator</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billCityName</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStateCode</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>postalCode</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>ssn</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>taxId</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>accountNumber</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     *     <tr>
     *     	   <td>passwordPin</td>
     *         <td>String</td>
     *         <td align=center>not used</td>
     *     <tr>
     * </table>
     * 
     * @return {@link com.tscp.mvno.api.APIPortResultResponseHolder}
     */
    @WebMethod
    public APIPortResultResponseHolder APIprePortValidation(String activity, PortInformation portInfo )
    {
    	String lApiName = "prePortValidation";
    	init();
        resetStatusMessage();
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIprePortValidation **** ");
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portInfo.MDN          :: "+portInfo.mdn+" **** ****");
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portInfo.TimeZone     :: "+portInfo.timeZone+" **** ****");
        }
        if( activeSprintConnection ) {
	        if( !webServiceConnection ) {
	        	APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	
		    	try{
		            createDBRequestEntry = new CreateDBRequestEntry(logger);
		    	   portResult = new PortResult();
		    	   portResultHolder = new PortResultHolder(portResult);
		    	   subMaintenance6.prePortValidation(key, activity, portInfo, portResultHolder);
		    	}catch(invalid_time_zone_exception time_ex){   	
		    		responseStr = buildStackTraceErrorResponse(time_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(time_ex.errorCode), lApiName, "invalid_time_zone_exception");
		       	}catch(invalid_mdn_exception mdn_ex){
		    		responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		    	}catch(missing_field_exception field_ex){
		    		responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		    	}catch(general_application_error_exception gen_ex){    		
		    		responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		    	}catch(Exception e){
		    	   responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		    	   statusStr ="FAIL";
		    	    
			    }finally{
			    	aPIPortResultResponseHolder = new APIPortResultResponseHolder(portResultHolder.value, responseStr, statusStr);
		             APIlogout(apikey);
			    }
	        } else { //Use Sprint's Web service connection
	        	try {
	        		aPIPortResultResponseHolder = apiConversionUtil.APIprePortValidation(mUserName, mOrderID, activity, portInfo);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				e.printStackTrace();
        				responseStr = api_ex.getMessage();
        			}
        			System.out.println("Testing 6");
        			aPIPortResultResponseHolder = new APIPortResultResponseHolder(null, responseStr, api_ex.getStatus());        			
        		}
	        }
        } else { // no active connection to Sprint
        	PortResult result = new PortResult();
        	result.mdn = portInfo.mdn;
        	result.numberPortabilityDirectionIndicator = "A";
        	result.portCsa = "LAXLAX323";
        	aPIPortResultResponseHolder = new APIPortResultResponseHolder(result,"Manually bypassed pre port vaidation","SUCCEED");
        }
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIprePortValidation ****");
        }
        createDBRequestEntry = null;        
    	return aPIPortResultResponseHolder;
    }
    
  //--------------------------------------------------------------------------------  
  
    /**
     * This method will reserve and MDN and will have to be followed by an 
     *         activateReserveSubscription method call in order to activate the reserved MDN.
     *          
     * @param pendingSubscription {@link com.spcs.pls.m2m.PendingSubscription}
     *  
     * @return APIReserveSubscriptionResponseHolder
     * 
     */
    @WebMethod
    public APIReserveSubscriptionResponseHolder APIreserveSubscription(PendingSubscription pendingSubscription)
    {
    	String lApiName = "reserveSubscription";
        resetStatusMessage();
        APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();   	

    	try{

            createDBRequestEntry = new CreateDBRequestEntry(logger);
            createDBRequestEntry.logDBRequest(pendingSubscription);
    	    pendingSubscriptionHolder = new PendingSubscriptionHolder(pendingSubscription);
            logger.log(LOG_EVENT_TYPE, LOG_INFO, " subMaintenance6 " + subMaintenance6); 
    	    subMaintenance6.reserveSubscription(key, pendingSubscriptionHolder);
            
    	}catch(invalid_mdn_exception mdn_ex){
    		responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		    statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
    	}catch(missing_field_exception field_ex){
    		responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
    	}catch(general_application_error_exception gen_ex ) {
    		responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
    		statusStr ="FAIL";
    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
    	}catch(Exception e){
    		 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
      	     statusStr ="FAIL";
    	}finally{
    		aPIReserveSubscriptionResponseHolder = 
    			new APIReserveSubscriptionResponseHolder(
    					pendingSubscriptionHolder.value,
    				    responseStr,
    				    statusStr);
             APIlogout(apikey);
    	}
    	    
    	return aPIReserveSubscriptionResponseHolder;
    }
    
    
   //  --------------------------------------------------------------------------------  
	
    /**
     * 
     * @param csa String
     * @return APIValidNPAListResponseHolder
     */
    @WebMethod
    public APIValidNPAListResponseHolder APIgetValidNPAList(String csa)
    {
    	 String lApiName = "getValidNPAList";
         resetStatusMessage();
    	 init();
    	 activity = null;
    	 validNPA = new ValidNPA();
    	 validNPAHolder = new ValidNPAHolder();
    	 validNPAHolder.value = validNPA;
         createDBRequestEntry = new CreateDBRequestEntry(logger);
    	 if( debugMode ) {
             logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIgetValidNPAList ****");
             logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String CSA    :: "+csa+" **** ****");    		
    	 }
    	 if( activeSprintConnection ) {
    		 if( !webServiceConnection ) {
	             APIKey apikey  = APIlogin();
	             String key = apikey.getKeyValue();
		    	 try{
		             createDBRequestEntry = new CreateDBRequestEntry(logger);
		    	    subMaintenance6.getValidNPAList(key, csa, activity, validNPAHolder);
		       	 }catch(invalid_value_exception value_ex){   	
		       		responseStr = buildStackTraceErrorResponse(value_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(value_ex.errorCode), lApiName, "invalid_value_exception");
		     	 }catch(missing_field_exception field_ex){
		     		responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		     	 }catch(general_application_error_exception gen_ex){     		
		     		responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		     	 }catch(Exception e){
		     		 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      	     statusStr ="FAIL";
				 }finally{
					 aPIValidNPAListResponseHolder = 
						 new APIValidNPAListResponseHolder(validNPAHolder.value, responseStr, statusStr);
		                         APIlogout(apikey);
				 }
    		 } else { //Using Sprint's Web Service Connection
    			 try {
    				 aPIValidNPAListResponseHolder = apiConversionUtil.APIgetValidNPAList(mUserName, mOrderID, csa);
    			} catch ( SprintAPIException api_ex ) {
		  		    statusStr ="FAIL";
		     		try {
		     			responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
		     		} catch( Exception e ) {
		     			responseStr = "Webservice invocation failed with error code :: "+api_ex.getWsError().getProviderError().getProviderErrorCode()+" with error text :: "+api_ex.getWsError().getProviderError().getProviderErrorText();
		     		}
		     		aPIValidNPAListResponseHolder = new APIValidNPAListResponseHolder(null,responseStr,statusStr);
    			}
    		 }
    	 } else { //no active Sprint Connection
    		 NPA npa = new NPA("323");
    		 try {
    		 npa.npaValue = csa.substring(6, csa.length());
    		 } catch (Exception e){
    		 } finally {
    		 validNPA.rowCount = "1";
    		 NPA[] npaList = new NPA[1];
    		 validNPA.validNPAList  = npaList;
    		 validNPA.validNPAList[0] = npa;
			 aPIValidNPAListResponseHolder = 
				 new APIValidNPAListResponseHolder(validNPAHolder.value, responseStr, statusStr);
    		 }
    	 }
         if( debugMode ) {
             logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIgetValidNPAList ****");
    	 }
     	 
     	return aPIValidNPAListResponseHolder;
    }
    
  //------------------------------------------------------------------------------
   // Introduced this method so that we can then retrieve the key from the session pool instead
    private APIKey APIlogin()
    {
     String loginId = null; 
     APIKey apiKey =   null; 

      if(aPISessionHelper != null)
      {
        apiKey = aPISessionHelper.getAPISession();
       }else
       {
          loginId =  APIlogin(null, null);
          apiKey = new APIKey(loginId);
       }

      return apiKey;
    }

 
    /**
     * Possible return values are a valid key or FAIL. Method will also initialize the CORBA interface
     * 
     * @param  UserId String
     * @param  Password String
     * @return String Key
     *  
     */
    public String APIlogin(String UserId, String Password)
    {
        resetStatusMessage();
    	String sessionKey = "";
        String user     =  ""; 
        String pwd      =  ""; 

        /* modified 11/19/2007 by dta
         * done to encapsulate the initialization of the APIInit object for use by the other methods.
        if( apiInit != null ){
           if(!apiInit.getInitStatus()){
             initAll();                      //initialize all interface
           } 
        }else{
          initAll();                      //initialize all interface
        }
        */
        init();
        
        if(apiInit.getsessionInit()){
          sessionKey = aPISessionHelper.getAPISession().getKeyValue();
        }else{
 
               if(isNull(UserId)){
                 user     = p.getProperty("MVNOUSERID"); 
               }else{
                 user = UserId;
               } 
               if(isNull(Password)){
                 pwd      = p.getProperty("MVNOPASSWORD");
               }else{
                 pwd = Password;
               }

           	try{
           	   sessionKey =  subMaintenance6.login(user, pwd);
           	}catch(Exception e){
    		 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
      	         statusStr ="FAIL";
                 sessionKey = responseStr;
           	}
        }//end else 

     return sessionKey;
    }
    
    
    /**
     * @param  UserId String
     * @param  Password String
     * @param  Key String
     * @return String confirmMsg
     */
    @WebMethod
    public APIGeneralResponseHolder APIlogout(String UserId, String Password, String key)
    {
        resetStatusMessage();
    	confirmMsg = new org.omg.CORBA.StringHolder();
        
    	try{
    		 subMaintenance6.logout(UserId, Password, key, confirmMsg);
    	}catch(logout_failed_exception failed_ex){
    		responseStr = buildStackTraceErrorResponse(failed_ex.getStackTrace());
    		statusStr ="FAIL";
    	}catch(Exception e){
    		 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
     	     statusStr ="FAIL";
    	}finally{
    		  aPIGeneralResponseHolder = 
				  new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
	
    	}
    	return aPIGeneralResponseHolder;
    }
   
//-------------------------------------------------------------------------------
    /**
     * Method will place key object back onto the stack.
     * 
     * @param  apikey APIKey
     *  
    */
   private void APIlogout(APIKey apikey)
    {
      if(apikey != null)
      {	
    	 if(apiInit.getsessionInit()){
    	    aPISessionHelper.putAPISession(apikey);
    	    logger.log(LOG_EVENT_TYPE, LOG_INFO, "Successfully released key to the pool: sessionId: " + apikey.getKeyValue()); 
    	  }else{
    		   logger.log(LOG_EVENT_TYPE, LOG_WARNING, "Unable to released key back to the pool: sessionId: " + apikey.getKeyValue() + " will be nullafied."); 
    		   APIlogout(apikey.getKeyValue());   // simply logout from sprint.
    		   apikey = null;                     // set to null, unable to push session back onto the stack
    	  }
      }else{
   	    logger.log(LOG_EVENT_TYPE, LOG_ERROR, "Unable to released key back to the pool: Key is Null"); 
   	  }
    }
    
  //-----------------------------------------------------------------------------
   /**
    * Method will log key out from sprint.
    * 
    * @param  key String - session key
    *  
    */
    private void APIlogout(String key)
    {
      String user     = p.getProperty("MVNOUSERID");
      String password = p.getProperty("MVNOPASSWORD");
      APIGeneralResponseHolder lougOutResponse = null;
      
      if(isNull(key))
      {
         lougOutResponse = APIlogout(user, password, key);
         if(lougOutResponse.apiResponseMessage.equalsIgnoreCase("successful")){
              logger.log(LOG_EVENT_TYPE, LOG_INFO, "Successfully logged out with userid: " +user + " password: "+ password + " sessionId: " + key); 
              logger.log(LOG_EVENT_TYPE, LOG_INFO, "Session key: " + key + " could not be released to the pool"); 
         }else{
             logger.log(LOG_EVENT_TYPE, LOG_ERROR, "Unable to log out with userid: " +user + " password: "+ password + " sessionId: " + key); 
         }
      }
    }//end method 
   
  //-----------------------------------------------------------------------------------
    /**
     * Method designed to invoke {@link com.spcs.pls.m2m._M2mSubMaintenance6.portInActivateSubscription}.  This method will send
     * the subscription object and the portInfo object to Sprint and return with a portID.
     * 
     * @param portInfo {@link com.spcs.pls.m2m.PortInformation} object which contains all relevent port information (required)
     * <table border> 
     *     <th>object_name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     	   <td>mdn</td>
     *         <td>String</td>
     *         <td align=center><i>optional</i> -ignored when used as input for portInActivateSubscription</td>
     *     <tr>
     *     <tr>
     *     	   <td>timeZone</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>authorizedBy</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>billFirstName</td>
     *         <td>String</td>
     *         <td align=center>required if businessName not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>billLastName</td>
     *         <td>String</td>
     *         <td align=center>required if businessName not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>businessName</td>
     *         <td>String</td>
     *         <td align=center>required if billFirstName and billLastName are not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetNumber</td>
     *         <td>String</td>
     *         <td align=center><i>optional</i></td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetName</td>
     *         <td>String</td>
     *         <td align=center><b>required</b></td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetDirectionIndicator</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     *     <tr>
     *     	   <td>billCityName</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStateCode</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>postalCode</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>ssn</td>
     *         <td>String</td>
     *         <td align=center>required when accountNumber or taxId is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>taxId</td>
     *         <td>String</td>
     *         <td align=center>required when accountNumber or ssn is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>accountNumber</td>
     *         <td>String</td>
     *         <td align=center>required when ssn or taxId is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>passwordPin</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     * </table>
     * @param sub {@link com.spcs.pls.m2m.Subscription} Object representing a pending Subscription (required)
     * <table border>
     *     <th>object_name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     	   <td>activity</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     *     <tr>
     *     	   <td>MDN</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>ESN</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>CSA</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>pricePlans</td>
     *         <td>{@link com.spcs.pls.m2m.Sali}</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>services</td>
     *         <td>{@link com.spcs.pls.m2m.Sali}[]</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     * </table>
     * 
     * @return APIPortInActivateSubscriptionResponseHolder
     */
    @WebMethod
    public APIPortInActivateSubscriptionResponseHolder APIportInActivateSubscription(PortInformation portInfo, Subscription sub )
    {
    	init();
    	String lApiName = "portInActivateSubscription";
        resetStatusMessage();
    	APIPortInActivateSubscriptionResponseHolder aPIPortInActivateSubscriptionResponseHolder;
    	portResult = new PortResult();
    	portResultHolder = new PortResultHolder(portResult);
    	subHolder = new SubscriptionHolder(sub);
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIportInActivateSubscription ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** PortInfo                                ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.AccountNumber    :: "+portInfo.accountNumber+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.AuthorizedBy     :: "+portInfo.authorizedBy+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillCityName     :: "+portInfo.billCityName+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillFirstName    :: "+portInfo.billFirstName+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillLastName     :: "+portInfo.billLastName+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillStateCode    :: "+portInfo.billStateCode+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillStreetInd    :: "+portInfo.billStreetDirectionIndicator+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillStreetName   :: "+portInfo.billStreetName+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BillStreetNumber :: "+portInfo.billStreetNumber+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.BusinessName     :: "+portInfo.businessName+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.MDN              :: "+portInfo.mdn+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.PasswordPin      :: "+portInfo.passwordPin+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.PostalCode       :: "+portInfo.postalCode+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.SSN              :: "+portInfo.ssn+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.TaxId            :: "+portInfo.taxId+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PortInfo.TimeZone         :: "+portInfo.timeZone+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** SubInfo                                 ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.Activity          :: "+sub.activity+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.CSA               :: "+sub.CSA+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.EffectiveDate     :: "+sub.effectiveDate+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.ESN               :: "+sub.ESN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.MDN               :: "+sub.MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.MSID              :: "+sub.MSID+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.PricePlans                **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.PricePlans.SvcCode       :: "+sub.pricePlans.svcCode+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.PricePlans.SvcEffDt      :: "+sub.pricePlans.svcEffDt+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.PricePlans.SvcExpDt      :: "+sub.pricePlans.svcExpDt+" **** ****");
            if( sub.services != null && sub.services.length > 0 ) {
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** SubInfo.Services                  **** ****");
            	for( int i = 0; i < sub.services.length; ++i ) {
	                logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.Services["+i+"].SvcCode      :: "+sub.services[i].svcCode+" **** ****");
	                logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.Services["+i+"].SvcEffDt     :: "+sub.services[i].svcEffDt+" **** ****");
	                logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** SubInfo.Services["+i+"].SvcExpDt     :: "+sub.services[i].svcExpDt+" **** ****");
            	}
            }
            
        }
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	try{
		    	  subMaintenance6.portInActivateSubscription(key, portInfo, subHolder, portResultHolder);
		    	}catch(invalid_time_zone_exception time_ex){
		    		responseStr = buildStackTraceErrorResponse(time_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(time_ex.errorCode), lApiName, "invalid_time_zone_exception");
		        }catch(invalid_esn_exception esn_ex){
		        	responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
		        }catch(invalid_mdn_exception mdn_ex){
		        	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		        	//responseStr = getErrorMessage(mdn_ex.errorCode);
		        	//responseStr += " Possible Solution: " + getResolutionMessage(mdn_ex.errorCode);
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		        }catch(invalid_date_exception date_ex){
		        	responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
		        }catch(invalid_csa_exception csa_ex){
		        	responseStr = buildStackTraceErrorResponse(csa_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(csa_ex.errorCode), lApiName, "invalid_csa_exception");
		        }catch(missing_field_exception field_ex){
		        	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		        }catch(general_application_error_exception gen_ex){
		        	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		        }catch(Exception e){
		        	 responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      	     statusStr ="FAIL";
			    }finally{
			    	aPIPortInActivateSubscriptionResponseHolder =
			    		new APIPortInActivateSubscriptionResponseHolder(
			    				subHolder.value,
			    				portResultHolder.value,
			    				responseStr,
			    				statusStr
			    				);
		                APIlogout(apikey);
			    }
    		} else { // use SPrint's web service connection
    			try {
    				aPIPortInActivateSubscriptionResponseHolder = apiConversionUtil.APIportInActivateSubscription(mUserName, mOrderID, portInfo, sub);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				System.out.println("Testing Activate WNP Port");
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIPortInActivateSubscriptionResponseHolder = new APIPortInActivateSubscriptionResponseHolder(null, null, responseStr, api_ex.getStatus());        			
        		}
    		}
    	} else { //no active connection to Sprint
    		portResultHolder.value.mdn = subHolder.value.MDN;
    		portResultHolder.value.oldServiceProvider = "Cingular";
    		portResultHolder.value.portID 				= "1122211";
    		portResultHolder.value.portCsa				= subHolder.value.CSA;
    		portResultHolder.value.desiredDueDateAndTime= "200711131702";
    		portResultHolder.value.numberPortabilityDirectionIndicator="A";
    		subHolder.value.MSID 						= "00000"+subHolder.value.MDN;
	    	aPIPortInActivateSubscriptionResponseHolder =
	    		new APIPortInActivateSubscriptionResponseHolder(
	    				subHolder.value,
	    				portResultHolder.value,
	    				"Manually simulated port-in Activation",
	    				"SUCCEED"
	    				);
    	}
    	if( debugMode ) {
    		if( aPIPortInActivateSubscriptionResponseHolder.portResult != null ) {
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortID                :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.portID );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortMdn               :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.mdn );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortCsa               :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.portCsa );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.DueDateTime           :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.desiredDueDateAndTime );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortDirection         :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.numberPortabilityDirectionIndicator );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.oldServiceProvider    :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.oldServiceProvider );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.permDialingBeginDt    :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.permissiveDialingPeriodBeginDate );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.permDialingEndDt      :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.permissiveDialingPeriodEndDate );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.portInStatus          :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.portInStatus );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.portInStatusText      :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.portInStatusText );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.ppvStatus             :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.ppvStatus );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.ppvStatusText         :: "+aPIPortInActivateSubscriptionResponseHolder.portResult.ppvStatusText );
    		}
    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIportInActivateSubscription ****");
    	}
        createDBRequestEntry = null;
	    return aPIPortInActivateSubscriptionResponseHolder;
    }
    
    
    //------------------------------------------------------------------------------
    
    /**
     * This Method is designed to provide end users with the ability to issue the swap MDN functionality while at the same time
     * porting in a number as the new number.
     * 
     * @param oldMDN String
     * @param newMDN String
     * @param portInformation {@link com.spcs.pls.m2m.PortInformation} object which contains all relevent port information (required)
     * <table border> 
     *     <th>object_name</th><th>object_type</th><th>usage</th>
     *     <tr>
     *     	   <td>mdn</td>
     *         <td>String</td>
     *         <td align=center><i>optional</i> -ignored when used as input for portInActivateSubscription</td>
     *     <tr>
     *     <tr>
     *     	   <td>timeZone</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>authorizedBy</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>billFirstName</td>
     *         <td>String</td>
     *         <td align=center>required if businessName not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>billLastName</td>
     *         <td>String</td>
     *         <td align=center>required if businessName not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>businessName</td>
     *         <td>String</td>
     *         <td align=center>required if billFirstName and billLastName are not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetNumber</td>
     *         <td>String</td>
     *         <td align=center><i>optional</i></td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetName</td>
     *         <td>String</td>
     *         <td align=center><b>required</b></td>
     *     <tr>
     *     <tr>
     *     	   <td>billStreetDirectionIndicator</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     *     <tr>
     *     	   <td>billCityName</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>billStateCode</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>postalCode</td>
     *         <td>String</td>
     *         <td align=center><b>required</td>
     *     <tr>
     *     <tr>
     *     	   <td>ssn</td>
     *         <td>String</td>
     *         <td align=center>required when accountNumber or taxId is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>taxId</td>
     *         <td>String</td>
     *         <td align=center>required when accountNumber or ssn is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>accountNumber</td>
     *         <td>String</td>
     *         <td align=center>required when ssn or taxId is not provided</td>
     *     <tr>
     *     <tr>
     *     	   <td>passwordPin</td>
     *         <td>String</td>
     *         <td align=center><i>optional</td>
     *     <tr>
     * </table>
     * 
     * @return APIPortInSwapMDNResponseHolder
     */
    @WebMethod
    public APIPortInSwapMDNResponseHolder APIportInSwapMDN(String oldMDN,  String newMDNIn, PortInformation portInformation)
    {
    	String lApiName = "portInSwapMDN";
        resetStatusMessage();
    	portResultHolder = new PortResultHolder(new PortResult());
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** BEGIN APIportInSwapMDN ****");
        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** oldMDN  :: "+oldMDN+" **** ****");
        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** newMDN  :: "+newMDNIn+" **** ****");
        }
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	activity = null;
		    	confirmMsg = new org.omg.CORBA.StringHolder();
		    	newMDN     = new org.omg.CORBA.StringHolder(newMDNIn);
		    	MSID       = new org.omg.CORBA.StringHolder();
		    	portResultHolder = new PortResultHolder(new PortResult());
		    	
		       try{
		           createDBRequestEntry = new CreateDBRequestEntry(logger);
		    	    subMaintenance6.portInSwapMDN(key, oldMDN, activity, portInformation, newMDN, portResultHolder, MSID, confirmMsg);
		    	}catch(invalid_csa_exception csa_ex){
		    		responseStr = buildStackTraceErrorResponse(csa_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(csa_ex.errorCode), lApiName, "invalid_csa_exception");
		        }catch(invalid_mdn_exception mdn_ex){
		        	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		        }catch(missing_field_exception field_ex){
		        	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		    		statusStr ="FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		        }catch(Exception e){
			        responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      	        statusStr ="FAIL";
			    }finally{
			    	aPIPortInSwapMDNResponseHolder = new
			    	     APIPortInSwapMDNResponseHolder(
			    	    		 newMDN.value, 
			    	    		 MSID.value, 
			    	    		 confirmMsg.value,
			    	    		 portResultHolder.value,
			    	    		 responseStr,
			    	    		 statusStr
			    	    		  );
		                APIlogout(apikey);
			    }
    		} else { /*use Sprint's web service connection
    			aPIPortInSwapMDNResponseHolder = new 
    				APIPortInSwapMDNResponseHolder(
    						"",//new mdn
    						"", //msid
    						"", //confirm msg
    						null, //Port Result Holder
    						"Web Service invocation has not been implemented",
    						APIConversionUtil.STATUS_FAIL
    					);
    			*/
    			try {
    				System.out.println("Testing PortInSwapMDN WNP Port");
    				//logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Testing PortInSwapMDN WNP Port ****");
    				aPIPortInSwapMDNResponseHolder = apiConversionUtil.APIportInSwapMDNRequest(mUserName, mOrderID, oldMDN, newMDNIn, portInformation);
                    } catch ( SprintAPIException api_ex ) {
            			try {
            				logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** SprintAPIException Caught in APIportInSwapMDN ****");
            				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
            			} catch( Exception e ) {
            				logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Exception Caught in APIportInSwapMDN ****");
            				responseStr = api_ex.getMessage();
            			}
            			aPIPortInSwapMDNResponseHolder = new APIPortInSwapMDNResponseHolder(null, null, null, null, responseStr, api_ex.getStatus());
        			}
        		}
    		}
    
    	if( debugMode ) {
    		if( aPIPortInSwapMDNResponseHolder.portResult != null ) {
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortID                :: "+aPIPortInSwapMDNResponseHolder.portResult.portID );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortMdn               :: "+aPIPortInSwapMDNResponseHolder.portResult.mdn );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortCsa               :: "+aPIPortInSwapMDNResponseHolder.portResult.portCsa );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.DueDateTime           :: "+aPIPortInSwapMDNResponseHolder.portResult.desiredDueDateAndTime );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.PortDirection         :: "+aPIPortInSwapMDNResponseHolder.portResult.numberPortabilityDirectionIndicator );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.oldServiceProvider    :: "+aPIPortInSwapMDNResponseHolder.portResult.oldServiceProvider );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.permDialingBeginDt    :: "+aPIPortInSwapMDNResponseHolder.portResult.permissiveDialingPeriodBeginDate );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.permDialingEndDt      :: "+aPIPortInSwapMDNResponseHolder.portResult.permissiveDialingPeriodEndDate );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.portInStatus          :: "+aPIPortInSwapMDNResponseHolder.portResult.portInStatus );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.portInStatusText      :: "+aPIPortInSwapMDNResponseHolder.portResult.portInStatusText );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.ppvStatus             :: "+aPIPortInSwapMDNResponseHolder.portResult.ppvStatus );
	    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** portResult.ppvStatusText         :: "+aPIPortInSwapMDNResponseHolder.portResult.ppvStatusText );
    		}
    		else {
    			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** aPIPortInSwapMDNResponseHolder.portResult returned null ****");
    		}
    		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIportInSwapMDN ****");
    	}
        createDBRequestEntry = null;
	    return aPIPortInSwapMDNResponseHolder;
    }
   //--------------------------------------------------------------------------------
    
    /**
     * @param MDN String – access number associated with the subscription.  (required)
     * @param effDt String  – the expiration date for the subscription.  (optional)
     * 
     * @return APIGeneralResponseHolder
     */
    @WebMethod
    public APIGeneralResponseHolder APIexpireSubscription(String MDN, String effDt)
    {
    	init();
    	String lApiName = "expireSubscription";
        resetStatusMessage();
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIexpireSubscription ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String MDN    :: "+MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String effDt  :: "+effDt+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	confirmMsg = new org.omg.CORBA.StringHolder();
		    	activity = null;
	    		try{
	    			createDBRequestEntry = new CreateDBRequestEntry(logger);
	    			subMaintenance6.expireSubscription(key, MDN, activity, effDt, confirmMsg);
			    }catch(invalid_mdn_exception mdn_ex){
			    	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	        		statusStr ="FAIL";
	          		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
			    }catch(invalid_date_exception date_ex){
			    	responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
	        		statusStr ="FAIL";
	            	responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
			    }catch(missing_field_exception field_ex){
			    	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
	        		statusStr ="FAIL";
	            	responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
			    }catch(general_application_error_exception gen_ex){
			    	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
	        		statusStr ="FAIL";
	            	responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
			    }catch(Exception e){
			    	responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      		statusStr ="FAIL";
			    }finally{
			    	try{
			    	    createDBRequestEntry.logDBchangeSubscription(activity, MDN, effDt, APIHelper.expireSubscription, confirmMsg.value);
			    	}catch(Exception e){
	                    responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			    	}
					aPIGeneralResponseHolder = 
						  new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
			
					APIlogout(apikey);
			    }
        	} else { // use Sprint's web service 
        		try {
        			aPIGeneralResponseHolder = apiConversionUtil.APIexpireSubscription(mUserName, mOrderID, MDN, effDt);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIGeneralResponseHolder = new APIGeneralResponseHolder(null,responseStr,api_ex.getStatus());
        		}
        	}
        } else { //no Active Sprint Connection 
        	statusStr 	= "SUCCEED";
        	responseStr = "Simulated execution of expireSubscription";
        	String responseStatus = "Manually completed execution of expireSubscription";
        	//confirmMsg.value = "Manually completed execution of expireSubscription";
			aPIGeneralResponseHolder = 
				  new APIGeneralResponseHolder(responseStatus, responseStr, statusStr);
        }
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIexpireSubscription ****");
        }
        return aPIGeneralResponseHolder;   
    }
    
    //---------------------------------------------------------------------------
    /**
     * @param String street 
     * @param String Xstreet 
     * @param String city  
     * @param  String state 
     * @param  String zip
     * @return APICsaResponseHolder
     */
    @WebMethod
    public APICsaResponseHolder APIgetCSA(String street, String Xstreet, String city, String state, String zip)
    {
    	init();
    	String lApiName = "getCSA";
        resetStatusMessage();
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIgetCSA ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String street   :: "+street+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String Xstreet  :: "+Xstreet+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String city     :: "+city+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String state    :: "+state+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String zip      :: "+zip+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		
		    	try{
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Initiated APIgetCSA ****");
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** String street   :: "+street+" **** ****");
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** String Xstreet  :: "+Xstreet+" **** ****");
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** String city     :: "+city+" **** ****");
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** String state    :: "+state+" **** ****");
		          //  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** String zip      :: "+zip+" **** ****");
		         //   createDBRequestEntry = new CreateDBRequestEntry(logger);
		    	   CSA =  subMaintenance6.getCSA(key, street, Xstreet, city, state, zip);
		    	}catch(missing_field_exception field_ex){    		
		    		 responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		  		     statusStr ="FAIL";
		    		 //  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		    	}catch(general_application_error_exception gen_ex){
		    		 responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		  		     statusStr ="FAIL";
		  		  // responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		    	}catch(Exception e){
		    		  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		     		  statusStr ="FAIL";
		    	}finally{
		    		aPICsaResponseHolder =new APICsaResponseHolder(CSA, responseStr, statusStr);
		                APIlogout(apikey);
		            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIgetCSA ****");
		    	}
        	} else { //use Sprint's web service
        		try {
        			aPICsaResponseHolder = apiConversionUtil.APIgetCSA(mUserName, mOrderID, street, Xstreet, city, state, zip);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPICsaResponseHolder = new APICsaResponseHolder(null,responseStr,api_ex.getStatus());
        		}
        	}
	    	
        }
        
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIgetCSA ****");
        }
        createDBRequestEntry = null;
        return aPICsaResponseHolder;
    }
    
    //---------------------------------------------------------------------------
    
    /**
     * @return APICSAListResponseHolder
     */
    @WebMethod
    public APICSAListResponseHolder APIgetCSAList()
    {
    	String lApiName = "getCSAList";
        resetStatusMessage();
    	activity = null;
    	csaListHolder = new CsaListHolder();
        createDBRequestEntry = new CreateDBRequestEntry(logger);
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
	            APIKey apikey  = APIlogin();
	            String key = apikey.getKeyValue();
		    	try{
		            logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Initiated APIgetCSAList ****");
		            createDBRequestEntry = new CreateDBRequestEntry(logger);
		    	   subMaintenance6.getCSAList(key, activity, csaListHolder);
		    	}catch(general_application_error_exception gen_ex){
		    		  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		     		  statusStr ="FAIL";
		     		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		       	}catch(Exception e){
		       	          responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		 		  statusStr ="FAIL";
		       	}finally{
		       		aPICSAListResponseHolder = new
		       		        APICSAListResponseHolder(csaListHolder.value, responseStr, statusStr);
		                APIlogout(apikey);
		            logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Initiated APIgetCSAList ****");
		       	}
    		} else { //WS Connection, not corba
    			try {
    				aPICSAListResponseHolder = apiConversionUtil.getCsaList(mUserName, mOrderID);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPICSAListResponseHolder = new APICSAListResponseHolder(null,responseStr,api_ex.getStatus());
        		}
    		}
    	}
        createDBRequestEntry = null;
        return aPICSAListResponseHolder;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * @param String csa
     * @return APISocListResponseHolder
     * 
     * @note: Given a CSA, method will retrieve the SOC list
     */
    @WebMethod
    public APISocListResponseHolder APIgetPpSoclist(String csa)
    {
        socProfileListHolder = new SocProfileListHolder();
        String lApiName = "getPpSoclist";
        resetStatusMessage();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		        activity = null;
		        try{
		            subMaintenance6.getPpSoclist(key, csa, activity, socProfileListHolder);
		        }catch(general_application_error_exception gen_ex){
		   	        responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		     		statusStr ="FAIL";
		        }catch(Exception e){
		   	        responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		   	        statusStr ="FAIL";
		        }finally{
		        	aPISocListResponseHolder = new 
		        	      APISocListResponseHolder(socProfileListHolder.value, responseStr, statusStr);
		                APIlogout(apikey);
		        }
        	} else { //Using Sprint's Web Service Connection
        		try {
        			aPISocListResponseHolder = apiConversionUtil.APIgetPpSoclist(mUserName, mOrderID, csa);

        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPISocListResponseHolder = new APISocListResponseHolder(null, responseStr, api_ex.getStatus());
        		}
        	}
        } 
    	createDBRequestEntry = null;
        return aPISocListResponseHolder;
    }
    
    //---------------------------------------------------------------------------
    /**
     * @param String MDNIn  
     * @return APIGetReserveSubscriptionResponseHolder
     * @note: This API is used to retrieve reserved subscription detail information based 
     *         on access number (MDNIn). 
     */
     @WebMethod
    public APIGetReserveSubscriptionResponseHolder APIgetReservedSubscription(String MDNIn)
    {
    	String lAPIName = "getReservedSubscription";
        resetStatusMessage();
        if( activeSprintConnection ) {
	        APIKey apikey  = APIlogin();
	        String key = apikey.getKeyValue();
	    	MDN              = new org.omg.CORBA.StringHolder(MDNIn);
	    	MSID             = new org.omg.CORBA.StringHolder();
	    	commSvcAreaId    = new org.omg.CORBA.StringHolder(); 
	    	majorAcctNbr     = new org.omg.CORBA.StringHolder(); 
	    	pricePlan        = new Sali2Holder(); 
	    	reserveMDNID     = new  org.omg.CORBA.StringHolder(); 
	    	rsvDt            = new  org.omg.CORBA.StringHolder(); 
	    	rsvSbscrpId      = new  org.omg.CORBA.StringHolder();
	    	rsvTm            = new  org.omg.CORBA.StringHolder(); 
	    	serviceList      = new SaliList2Holder();
	    	
	      	try{
	    	resellerV2SubInquiry.getReservedSubscription(key, 
	    			                                      MDN, 
	    			                                      commSvcAreaId, 
	    			                                      MSID, 
	    			                                      majorAcctNbr, 
	    			                                      pricePlan, 
	    			                                      reserveMDNID, 
	    			                                      rsvDt, 
	    			                                      rsvSbscrpId, 
	    			                                      rsvTm, 
	    			                                      serviceList);
	    	}catch(invalid_mdn_exception mdn_ex){
	    		     responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	 		     statusStr ="FAIL";
	    	}catch(general_application_error_exception gen_ex){
	    		    responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
	 		    statusStr ="FAIL";
	    	}catch( missing_field_exception field_ex){
	    		    responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
	 		    statusStr ="FAIL";
	    	}catch(Exception e){
	    		     responseStr = buildStackTraceErrorResponse(e.getStackTrace());
	 		     statusStr ="FAIL";
	    	}finally{
	    		aPIGetReserveSubscriptionResponseHolder =
	    			new APIGetReserveSubscriptionResponseHolder(MDN.value, 
						                                        commSvcAreaId.value, 
						                                        MSID.value, 
						                                        majorAcctNbr.value, 
						                                        pricePlan.value,
						                                        reserveMDNID.value, 
										        rsvDt.value, 
										        rsvSbscrpId.value, 
									                rsvTm.value, 
										        serviceList.value,
										        responseStr,
										        statusStr
					                                                );
	             APIlogout(apikey);
	    	}
        }
    	return aPIGetReserveSubscriptionResponseHolder;
    }
     
    @WebMethod
    public APIResellerReservedSubscriptionResponseHolder APIResellerV2ReservedSubscriptionInquiry(String iMdn) {
    	APIResellerReservedSubscriptionResponseHolder responseHolder = new APIResellerReservedSubscriptionResponseHolder();
    	String lApiName = "getReservedSubscription";
    	init();
    	resetStatusMessage();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIResellerV2ReservedSubscriptionInquiry ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MDN                :: "+iMdn+" **** ****");//CODE FIX IN DEV,
                                                                                                       //APPLY TO NEXT 
                                                                                                       //PRODUCTION RELEASE
        }
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
    			APIKey apiKey = APIlogin();
    			String key = apiKey.getKeyValue();
    			try {
    				org.omg.CORBA.StringHolder			MDN				= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder 			commSvcAreaId 	= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder 			MSID			= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder 			majorAcctNbr	= new org.omg.CORBA.StringHolder();
    				com.spcs.pls.m2m.Sali2Holder 		pricePlan		= new com.spcs.pls.m2m.Sali2Holder();
    				org.omg.CORBA.StringHolder			reserveMDNID	= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder			rsvDt			= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder			rsvSbscrpId		= new org.omg.CORBA.StringHolder();
    				org.omg.CORBA.StringHolder			rsvTm			= new org.omg.CORBA.StringHolder();
    				com.spcs.pls.m2m.SaliList2Holder	serviceList		= new com.spcs.pls.m2m.SaliList2Holder();
    				
    				
    				resellerV2SubInquiry.getReservedSubscription(
    							key, 
    							MDN, 
    							commSvcAreaId, 
    							MSID, 
    							majorAcctNbr, 
    							pricePlan, 
    							reserveMDNID, 
    							rsvDt, 
    							rsvSbscrpId, 
    							rsvTm, 
    							serviceList);
    				
    				responseHolder.setCommSvcAreaId(commSvcAreaId);
    				responseHolder.setMajorAcctNbr(majorAcctNbr);
    				responseHolder.setMDN(MDN);
    				responseHolder.setMSID(MSID);
    				responseHolder.setPricePlan(pricePlan);
    				responseHolder.setReserveMDNID(reserveMDNID);
    				responseHolder.setRsvDt(rsvDt);
    				responseHolder.setRsvSbscrpId(rsvSbscrpId);
    				responseHolder.setRsvTm(rsvTm);
    				responseHolder.setServiceList(serviceList);
    				
		    	} catch ( com.spcs.pls.m2m.invalid_mdn_exception mdn_ex ) {
			    	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	        		statusStr ="FAIL";
	          		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		        }catch(missing_field_exception field_ex){
		        	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		        }catch(general_application_error_exception gen_ex){
		        	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		    	} catch ( Exception e ) {
			        responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			        e.printStackTrace();
			        statusStr ="FAIL";
		    	}
    		} else { // Use Sprint's Web Service Connection 
    			try {
    				responseHolder = apiConversionUtil.APIResellerV2ReservedSubscriptionInquiry(mUserName, mOrderID, iMdn);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			responseHolder = new APIResellerReservedSubscriptionResponseHolder();
        			responseHolder.statusMessage = api_ex.getStatus();
        			responseHolder.responseMessage = responseStr;
        		}
    		}
    	}
        if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIResellerV2ReservedSubscriptionInquiry ****");
        }
    	createDBRequestEntry = null;
    	return responseHolder;
    }
     
    @WebMethod
    public APIResellerSubInquiryResponseHolder APIresellerV2SubInquiry(String ESN, String MDN) {
    	String lApiName = "getResellerSubscription";
    	init();
    	resetStatusMessage();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        APIResellerSubInquiryResponseHolder responseHolder = new APIResellerSubInquiryResponseHolder();
        if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIresellerV2SubInquiry ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** ESN                :: "+ESN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MDN                :: "+MDN+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		        try {
		    		com.spcs.pls.m2m.AccessEqpAsgmSeqHolder accessEqpAsgmList 	= new com.spcs.pls.m2m.AccessEqpAsgmSeqHolder();
		    		org.omg.CORBA.BooleanHolder areMoreAccessEqpAsgms 			= new org.omg.CORBA.BooleanHolder();
		    		com.spcs.pls.m2m.AccessNbrAsgmSeqHolder accessNbrAsgmList 	= new com.spcs.pls.m2m.AccessNbrAsgmSeqHolder();
		    		org.omg.CORBA.BooleanHolder areMoreAccessNbrAsgms 			= new org.omg.CORBA.BooleanHolder();
		    		org.omg.CORBA.StringHolder celulrActvtnDt 					= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.StringHolder commSvcAreaId 					= new org.omg.CORBA.StringHolder(); 
		    		com.spcs.pls.m2m.AccessChnlAsgmSeqHolder currentAccessChnlAsgmList = new com.spcs.pls.m2m.AccessChnlAsgmSeqHolder();
		    		com.spcs.pls.m2m.AccessChnlAsgmSeqHolder expiredAccessChnlAsgmList = new com.spcs.pls.m2m.AccessChnlAsgmSeqHolder();
		    		org.omg.CORBA.StringHolder IXC 								= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.StringHolder IXCSvcPrvdrEffDt					= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.BooleanHolder areMorePricePlans				= new org.omg.CORBA.BooleanHolder();
		    		com.spcs.pls.m2m.PLSSaliListHolder pricePlanList			= new com.spcs.pls.m2m.PLSSaliListHolder();
		    		org.omg.CORBA.StringHolder sbscrpSvcEffDt					= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.StringHolder sbscrpSvcExprDt					= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.StringHolder sbscrpTypeCd						= new org.omg.CORBA.StringHolder();
		    		org.omg.CORBA.BooleanHolder areMoreServices					= new org.omg.CORBA.BooleanHolder();
		    		com.spcs.pls.m2m.PLSSaliListHolder serviceList				= new com.spcs.pls.m2m.PLSSaliListHolder();
		    		
		    		resellerV2SubInquiry.getResellerSubscription(
		    					key
		    					, ESN
		    					, MDN
		    					, accessEqpAsgmList
		    					, areMoreAccessEqpAsgms
		    					, accessNbrAsgmList
		    					, areMoreAccessNbrAsgms
		    					, celulrActvtnDt
		    					, commSvcAreaId
		    					, currentAccessChnlAsgmList
		    					, expiredAccessChnlAsgmList
		    					, IXC
		    					, IXCSvcPrvdrEffDt
		    					, areMorePricePlans
		    					, pricePlanList
		    					, sbscrpSvcEffDt
		    					, sbscrpSvcExprDt
		    					, sbscrpTypeCd
		    					, areMoreServices
		    					, serviceList);
		    		
			    	responseHolder.setAccessEqpAsgmList(accessEqpAsgmList);
			    	responseHolder.setAccessNbrAsgmList(accessNbrAsgmList);
			    	responseHolder.setAreMoreAccessEqpAsgms(areMoreAccessEqpAsgms);
			    	responseHolder.setAreMoreAccessNbrAsgms(areMoreAccessNbrAsgms);
			    	responseHolder.setAreMorePricePlans(areMorePricePlans);
			    	responseHolder.setAreMoreServices(areMoreServices);
			    	responseHolder.setCelulrActvtnDt(celulrActvtnDt);
			    	responseHolder.setCommSvcAreaId(commSvcAreaId);
			    	responseHolder.setCurrentAccessChnlAsgmList(currentAccessChnlAsgmList);
			    	responseHolder.setExpiredAccessChnlAsgmList(expiredAccessChnlAsgmList);
			    	responseHolder.setIXC(IXC);
			    	responseHolder.setIXCSvcPrvdrEffDt(IXCSvcPrvdrEffDt);
			    	responseHolder.setPricePlanList(pricePlanList);
			    	responseHolder.setSbscrpSvcEffDt(sbscrpSvcEffDt);
			    	responseHolder.setSbscrpSvcExprDt(sbscrpSvcExprDt);
			    	responseHolder.setSbscrpTypeCd(sbscrpTypeCd);
			    	responseHolder.setServiceList(serviceList);
		    	} catch ( com.spcs.pls.m2m.invalid_mdn_exception mdn_ex ) {
			    	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	        		statusStr ="FAIL";
	          		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		    	} catch ( com.spcs.pls.m2m.invalid_esn_exception esn_ex ) {
		    		responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
		    		statusStr = "FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
		        }catch(missing_field_exception field_ex){
		        	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		        }catch(general_application_error_exception gen_ex){
		        	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		    	} catch ( Exception e ) {
			        responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			        e.printStackTrace();
			        statusStr ="FAIL";
		    	}
        	} else { //use Sprint's web service
        		try {
        			responseHolder = apiConversionUtil.APIresellerV2SubInquiry(mUserName, mOrderID, ESN, MDN);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				e.printStackTrace();
        				responseStr = api_ex.getMessage();
        			}
        			responseHolder = new APIResellerSubInquiryResponseHolder();
        			responseHolder.statusMessage = api_ex.getStatus();
        			responseHolder.responseMessage= responseStr;
        		} 
        	}
	    	
        } else {
        	responseHolder.statusMessage 	= "SUCCEED";
        	responseHolder.responseMessage 	= "Artificially invoked Sub Inquiry";
        }
    	responseHolder.statusMessage 	= statusStr;
    	responseHolder.responseMessage	= responseStr;
        if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIresellerV2SubInquiry ****");
        }
    	createDBRequestEntry = null;
    	return responseHolder;
    }
    
    
    @WebMethod
    public APIResellerUsageInquiryRepsonseHolder APIResellerUsageInquiry(String iMDN, String iESN, String iUsageType, String iFromDate, String iToDate, String iPrevEndingCallDt, String iPrevEndingCallTm) {
    	APIResellerUsageInquiryRepsonseHolder responseHolder = new APIResellerUsageInquiryRepsonseHolder();
    	String lApiName = "usageInquiry";
    	init();
    	resetStatusMessage();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
    	if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIResellerUsageInquiry ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** ESN                :: "+iESN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MDN                :: "+iMDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** UsageType          :: "+iUsageType+" **** ****");

            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** FromDate           :: "+iFromDate+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** ToDate             :: "+iToDate+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PrevEndingCallDt   :: "+iPrevEndingCallDt+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** PrevEndingCallTm   :: "+iPrevEndingCallTm+" **** ****");
    	}
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
	            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Key                :: "+key+" **** ****");
		        com.spcs.pls.m2m.UsageCategory  usageCategory		= new com.spcs.pls.m2m.UsageCategory();
		        org.omg.CORBA.StringHolder		MDN					= new org.omg.CORBA.StringHolder();
		        org.omg.CORBA.StringHolder		fromDt				= new org.omg.CORBA.StringHolder();
		        org.omg.CORBA.StringHolder		toDt				= new org.omg.CORBA.StringHolder();
		        org.omg.CORBA.StringHolder		usageType			= new org.omg.CORBA.StringHolder();
		        org.omg.CORBA.IntHolder			totalCalls			= new org.omg.CORBA.IntHolder();
		        org.omg.CORBA.DoubleHolder		totalAirtimeUsage	= new org.omg.CORBA.DoubleHolder();
		        org.omg.CORBA.BooleanHolder		areMoreCallDetails	= new org.omg.CORBA.BooleanHolder();
		        com.spcs.pls.m2m.CallDetailSeqHolder callDetailList = new com.spcs.pls.m2m.CallDetailSeqHolder();
    			try {	
			        String ESN = "";
			        String prevEndingCallDt = "";
			        String prevEndingCallTm = "";   
			        callDetailList.value = new com.spcs.pls.m2m.CallDetail[5];
			        ESN = iESN;
			        if( iMDN != null ) {
			        	MDN.value = iMDN;
			        } else {
			        	MDN.value = "";
			        }
			        if( iUsageType != null ) {
			        	usageType.value = iUsageType;
			        } else {
			        	usageType.value = "";
			        }
			        if( iFromDate != null )  {
			        	fromDt.value = iFromDate;
			        } else {
			        	fromDt.value = "";
			        }
			        if( iToDate != null ) {
			        	toDt.value = iToDate;
			        } else {
			        	toDt.value = "";
			        }
			        prevEndingCallDt = iPrevEndingCallDt;
			        prevEndingCallTm = iPrevEndingCallTm;
			        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** APIResellerUsageInquiry before CORBA invocation ****");
	    			resellerV2UsageInquiry.usageInquiry(
	    						key, 
	    						ESN, 
	    						prevEndingCallDt, 
	    						prevEndingCallTm, 
	    						usageCategory, 
	    						MDN, 
	    						fromDt, 
	    						toDt, 
	    						usageType, 
	    						totalCalls, 
	    						totalAirtimeUsage, 
	    						areMoreCallDetails, 
	    						callDetailList);
	    			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** APIResellerUsageInquiry After CORBA Invocation****");
    			} catch ( com.spcs.pls.m2m.invalid_mdn_exception mdn_ex ) {
			    	responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	        		statusStr ="FAIL";
	          		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		    	} catch ( com.spcs.pls.m2m.invalid_esn_exception esn_ex ) {
		    		responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
		    		statusStr = "FAIL";
		    		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
		        }catch(general_application_error_exception gen_ex){
		        	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		        } catch ( com.spcs.pls.m2m.invalid_date_exception date_ex ) {
		        	responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
		  		    statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
		    	} catch ( Exception e ) {
			        responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			        e.printStackTrace();
			        statusStr ="FAIL";
		    	} finally {
	    			responseHolder = new APIResellerUsageInquiryRepsonseHolder();
	    			responseHolder.statusMessage = statusStr;
	    			responseHolder.responseMessage = responseStr;
	    			responseHolder.setAreMoreCallDetails(areMoreCallDetails);
	    			responseHolder.setCallDetailList(callDetailList.value);
	    			responseHolder.setFromDt(fromDt);
	    			responseHolder.setToDt(toDt);
	    			responseHolder.setUsageType(usageType);
	    			responseHolder.setUsageCategory(usageCategory);
			        usageCategory		= null;
			        MDN					= null;
			        fromDt				= null;
			        toDt				= null;
			        usageType			= null;
			        totalCalls			= null;
			        totalAirtimeUsage	= null;
			        areMoreCallDetails	= null;
			        callDetailList 		= null;
		             APIlogout(apikey);
		    	}
    		} else { //use Sprint's WebService
    			try{
    				responseHolder = apiConversionUtil.APIResellerUsageInquiry(mUserName, mOrderID, iMDN, iESN, iUsageType, iFromDate, iToDate, iPrevEndingCallDt, iPrevEndingCallTm);
    			} catch ( SprintAPIException api_ex ) {
		  		    statusStr ="FAIL";
		     		try {
		     			responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
		     		} catch( Exception e ) {
		     			responseStr = "Webservice invocation failed with error code :: "+api_ex.getWsError().getProviderError().getProviderErrorCode()+" with error text :: "+api_ex.getWsError().getProviderError().getProviderErrorText();
		     		}
		     		responseHolder = new APIResellerUsageInquiryRepsonseHolder();
		     		responseHolder.statusMessage = api_ex.getStatus();
		     		responseHolder.responseMessage = responseStr;
    			}
    		}
    	}
    	if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIResellerUsageInquiry ****");
    	}
    	createDBRequestEntry = null;
    	return responseHolder;
    }
    
    //----------------------------------------------------------------------------
    
    
    
    /**
     * @param  String key
     * @param  PendingSubscriptionNPA subNPA
     * @return APIPendingSubscriptionNPAResponseHolder
     * @note:  Should probably only be used when reserving an MDN using an NPA
     */
    @WebMethod
    public APIPendingSubscriptionNPAResponseHolder APIreserveSubscriptionNPA(PendingSubscriptionNPA subNPA)
    {
    	init();
    	String lApiName = "reserveSubscriptionNPA";
        resetStatusMessage();	
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) { 
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIreserveSubscriptionNPA ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA Activity    :: "+subNPA.activity+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA CSA         :: "+subNPA.CSA+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA NPA         :: "+subNPA.NPA+" **** ****");
            Sali2 lSali2 = subNPA.pricePlans;
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** subNPA svcCode     :: "+lSali2.svcCode+" **** ****");
            Sali2[] prodList = subNPA.services;
            if( prodList != null && prodList.length > 0 ) {
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Service List **** ****");
	            for( int i = 0; i < prodList.length; i++ ) {
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcCode::"+prodList[i].svcCode+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcEffDt::"+prodList[i].svcEffDt+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** services["+i+"].svcExpDt::"+prodList[i].svcExpDt+" **** **** ****");
	            }
	        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Done Service List **** ****");
            }
        }
        if( activeSprintConnection ) { 
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		        
		    	try{
		            pendingSubscriptionNPAHolder = new PendingSubscriptionNPAHolder(subNPA);
		            if(subMaintenance6 == null){
		              logger.log(LOG_EVENT_TYPE, LOG_ERROR, "Orb is NOT Initialized "); 
		            }
		            createDBRequestEntry.logDBRequest(subNPA);
		    	        subMaintenance6.reserveSubscriptionNPA(key, pendingSubscriptionNPAHolder);
		                
		    	}catch(invalid_mdn_exception mdn_ex){
		    		 int i = mdn_ex.errorCode;
		    		 logger.log(LOG_EVENT_TYPE, LOG_ERROR, " INVALID MDN EXCEPTION::ERRORCODE:"+i);
		    		 responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		  		     statusStr ="FAIL";
		        }catch(missing_field_exception field_ex){
		        	 responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		  		     statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		        }catch(general_application_error_exception gen_ex){
		        	 responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		  		     statusStr ="FAIL";
		     		responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		        }catch(Exception e){
		          responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		          statusStr ="FAIL";
		        }finally{
			         aPIPendingSubscriptionNPAResponseHolder = 
			        	 new APIPendingSubscriptionNPAResponseHolder(
			        			 pendingSubscriptionNPAHolder.value,
			        			 responseStr, 
			        			 statusStr);
		             APIlogout(apikey);
		        }
        	} else { //use Sprint's Web Service
        		try {
        			aPIPendingSubscriptionNPAResponseHolder = apiConversionUtil.APIreserveSubscriptionNPA(mUserName, mOrderID, subNPA);
        			if( debugMode ) {
        	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** pendingSubscriptionNPAHolder properties: ****");
        	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** CSA     : "+aPIPendingSubscriptionNPAResponseHolder.subNPA.CSA+" **** ****");
        	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** MDN     : "+aPIPendingSubscriptionNPAResponseHolder.subNPA.MDN+" **** ****");
        	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** MSID    : "+aPIPendingSubscriptionNPAResponseHolder.subNPA.MSID+" **** ****");
        	        	logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** effDate : "+aPIPendingSubscriptionNPAResponseHolder.subNPA.effectiveDate+" **** ****");
        			}
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIPendingSubscriptionNPAResponseHolder = new APIPendingSubscriptionNPAResponseHolder(null,responseStr,api_ex.getStatus());
        		}
        	}
        } else {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Testing faked reservation ****");
            pendingSubscriptionNPAHolder = new PendingSubscriptionNPAHolder(subNPA);
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** pendingSubscriptionNPAHolder initialized ****");
            pendingSubscriptionNPAHolder.value = getNewMDN(pendingSubscriptionNPAHolder);
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** pendingSubscriptionNPAHolder value bound by getNewMDN ****");

        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** pendingSubscriptionNPAHolder properties: ****");
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MDN     : "+pendingSubscriptionNPAHolder.value.MDN+" **** ****");
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MSID    : "+pendingSubscriptionNPAHolder.value.MSID+" **** ****");
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** effDate : "+pendingSubscriptionNPAHolder.value.effectiveDate+" **** ****");
	        aPIPendingSubscriptionNPAResponseHolder = 
	        	 new APIPendingSubscriptionNPAResponseHolder(
	        			 pendingSubscriptionNPAHolder.value,
	        			 "Artificial TN Retrieved from internal systems", 
	        			 "SUCCEED");
        }
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIreserveSubscriptionNPA ****");
        }
    	createDBRequestEntry = null;
        return aPIPendingSubscriptionNPAResponseHolder;
    }

    //----------------------------------------------------------------------------
    
    /**
     * @param key String (required)
     * @param MDN String (required)
     * @return APIGeneralResponseHolder
     * @throws InterruptedException 
     */
    @WebMethod
    public APIGeneralResponseHolder APIrestoreSubscription(String MDN) throws InterruptedException
    {
    	String lApiName = "restoreSubscription";
    	init();
        resetStatusMessage();
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        APIGeneralResponseHolder TempAPIGeneralResponseHolder = aPIGeneralResponseHolder;
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIrestoreSubscription ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String MDN        :: "+MDN+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	activity = null;
		    	confirmMsg = new org.omg.CORBA.StringHolder();
		    	try{
		    		if( validateOutboundRequest ) {
		    			String lActivity = APIValidationUtil.MODIFY_SUB_RESTORE;
		    			APIValidationUtil validationUtil = new APIValidationUtil(logger);
		    			APIGeneralResponseHolder validationResponse = validationUtil.validateSubscriptionModificationRequest(key, createDBRequestEntry, MDN, lActivity, resellerV2SubInquiry);
		    			if( validationResponse != null ) {
		    				if( validationResponse.statusMessage.equals(APIValidationUtil.RESPONSE_STATUS_SUCCEED) ) {
		    					subMaintenance6.restoreSubscription(key, MDN, activity, confirmMsg);
		    				} else {
		    					responseStr = validationResponse.responseMessage;
		    				}
		    			} else {
		    				subMaintenance6.restoreSubscription(key, MDN, activity, confirmMsg);
		    			}
		    		} else {
		    			subMaintenance6.restoreSubscription(key, MDN, activity, confirmMsg);
		    		}
		    	}catch(invalid_mdn_exception mdn_ex){
		    		responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		         	statusStr ="FAIL";
				}catch(missing_field_exception field_ex){
				  	responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
			      	statusStr ="FAIL";
				}catch(general_application_error_exception gen_ex){
				   	responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
			      	statusStr ="FAIL";
			    }catch(Exception e){
				    responseStr = buildStackTraceErrorResponse(e.getStackTrace());
			      	statusStr ="FAIL";
	            } finally {
		            try{
		               createDBRequestEntry.logDBchangeSubscription(activity, MDN, null, APIHelper.restoreSubscription, confirmMsg.value);
		            }catch(Exception e){
		               responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		            }
		            aPIGeneralResponseHolder = 
						new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
		            APIlogout(apikey);
		        }
        	} else { //use Sprint's web service  
        		try {
        			if( validateOutboundRequest ) {
        				String lActivity = APIValidationUtil.MODIFY_SUB_RESTORE;
        				APIValidationUtil validationUtil = new APIValidationUtil(logger);
        				APIGeneralResponseHolder validationResponse = validationUtil.validateSubscriptionModificationRequest(mUserName, mOrderID, MDN, lActivity, createDBRequestEntry);
        				if( validationResponse != null ) {
        					if( validationResponse.statusMessage.equals(APIValidationUtil.RESPONSE_STATUS_SUCCEED) ) {
        						aPIGeneralResponseHolder = apiConversionUtil.APIrestoreSubscription(mUserName, mOrderID, MDN);
        					} else {
        						aPIGeneralResponseHolder = validationResponse;
        						aPIGeneralResponseHolder.statusMessage = APIValidationUtil.RESPONSE_STATUS_SUCCEED;
        						aPIGeneralResponseHolder.responseMessage = validationResponse.responseMessage;
        					}
        				} else {
        					aPIGeneralResponseHolder = apiConversionUtil.APIrestoreSubscription(mUserName, mOrderID, MDN);
        				}
        			} else {
        				aPIGeneralResponseHolder = apiConversionUtil.APIrestoreSubscription(mUserName, mOrderID, MDN);
        			}
        			
        			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Status Message        :: "+aPIGeneralResponseHolder.statusMessage+" **** ] ****");
        			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Response Message        :: "+aPIGeneralResponseHolder.responseMessage+" **** ] ****");
        			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** API Response Message        :: "+aPIGeneralResponseHolder.apiResponseMessage+" **** ] ****");
        		
        			TempAPIGeneralResponseHolder = new APIGeneralResponseHolder(aPIGeneralResponseHolder.apiResponseMessage,
        					aPIGeneralResponseHolder.responseMessage,aPIGeneralResponseHolder.statusMessage);
        			
					waiting();
					
        			restorePricePlanCheck(MDN); 
        			
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIGeneralResponseHolder = new APIGeneralResponseHolder(null, responseStr, api_ex.getStatus());
        		}
        	}
        } else {
        	aPIGeneralResponseHolder = new APIGeneralResponseHolder("SUCCEED","Manually bypassed restore process","SUCCEED");
        }
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIrestoreSubscription ****");
        }
        
        
        aPIGeneralResponseHolder = TempAPIGeneralResponseHolder;
        //RETURNED STATUS MESSAGES
        //logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return Status Message        :: "+aPIGeneralResponseHolder.statusMessage+" **** ] ****");
		//logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return Response Message        :: "+aPIGeneralResponseHolder.responseMessage+" **** ] ****");
		//logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return API Response Message        :: "+aPIGeneralResponseHolder.apiResponseMessage+" **** ] ****");
	    return aPIGeneralResponseHolder;
   	}
    
  //---------------------------------------------------------------------------------
    /**
     * This API provides the ability to swap the access number on an existing Reseller subscription when an NPA split occurs. 
     * 
     * @param key String 
     * @param oldMDN String 
     * @param activity String 
     * @return APISplitNpaMDNResponseHolder
     */ 
    @WebMethod
    public APISplitNpaMDNResponseHolder APIsplitNpaMDN(String oldMDN, String activity)
    {
        resetStatusMessage();
        APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();
    	org.omg.CORBA.StringHolder  newMDN     = new org.omg.CORBA.StringHolder(); 
    	org.omg.CORBA.StringHolder  MSID       = new org.omg.CORBA.StringHolder(); 
    	org.omg.CORBA.StringHolder  confirmMsg = new org.omg.CORBA.StringHolder();
    	
       try{
    	   subMaintenance6.splitNpaMdn(key, oldMDN, activity, newMDN, MSID, confirmMsg);
       }catch(Exception e){
    	   responseStr = buildStackTraceErrorResponse(e.getStackTrace());
   		   statusStr ="FAIL";
       }finally{
    	    aPISplitNpaMDNResponseHolder =
    		   new APISplitNpaMDNResponseHolder(newMDN.value, MSID.value, confirmMsg.value, responseStr, statusStr);
       }
       
       return aPISplitNpaMDNResponseHolder;
    }
    //-----------------------------------------------------------------------------
    /**
     * @param changePortInDueDateAndTimeInformation {@link com.spcs.pls.m2m.ChangePortInDueDateAndTimeInformation}  
     * @return APIChangePortInDueDateAndTimeResultResponseHolder
     */
     @WebMethod
    public APIChangePortInDueDateAndTimeResultResponseHolder APIchangePortInDueDateAndTime(
    		                                  ChangePortInDueDateAndTimeInformation changePortInDueDateAndTimeInformation)
    {
    	String lApiName = "changePortInDueDateAndTime"; 
        resetStatusMessage();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	changePortInDueDateAndTimeResult = new ChangePortInDueDateAndTimeResult();
		        changePortInDueDateAndTimeResultHolder =
		    	 new   ChangePortInDueDateAndTimeResultHolder(changePortInDueDateAndTimeResult);
		     
		    	try{
		    		subMaintenance6.changePortInDueDateAndTime(
		    			key, 
		    			changePortInDueDateAndTimeInformation, 
		    			changePortInDueDateAndTimeResultHolder);
		    	
		    	}catch(invalid_mdn_exception mdn_ex){
		    		  responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		      		  statusStr ="FAIL";
		      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
			  	}catch(invalid_time_zone_exception time_ex){
			  		 responseStr = buildStackTraceErrorResponse(time_ex.getStackTrace());
		     		         statusStr ="FAIL";
		     	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(time_ex.errorCode), lApiName, "invalid_time_zone_exception");
				}catch(invalid_value_exception value_ex){
					  responseStr = buildStackTraceErrorResponse(value_ex.getStackTrace());
		      		          statusStr ="FAIL";
		      	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(value_ex.errorCode), lApiName, "invalid_value_exception");
				}catch(general_application_error_exception gen_ex){
					  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		      		          statusStr ="FAIL";
		      	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
			    }catch(Exception e){
			    	          responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      		          statusStr ="FAIL";
			    }finally{
			    	  
				      		    	   
				          aPIChangePortInDueDateAndTimeResultResponseHolder =
				    	      new APIChangePortInDueDateAndTimeResultResponseHolder(
				    			       changePortInDueDateAndTimeResultHolder.value,
				    			       responseStr,
				    			       statusStr);
				          
				        
		                          APIlogout(apikey);
			    }
        	} else {
        		try {
        			aPIChangePortInDueDateAndTimeResultResponseHolder = apiConversionUtil.APIchangePortInDueDateAndTime(mUserName, mOrderID, changePortInDueDateAndTimeInformation);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIChangePortInDueDateAndTimeResultResponseHolder = new APIChangePortInDueDateAndTimeResultResponseHolder(null, responseStr, api_ex.getStatus());
        		}
        	}
        }   
    	createDBRequestEntry = null;
	    return aPIChangePortInDueDateAndTimeResultResponseHolder;
    }
    
    //------------------------------------------------------------------------------
    
    /**
     * This method provides the same functionality that changeServices provides but also
     *        gives you the ability to update the CallForwarding number
     * @param serviceSub2 {@link com.spcs.pls.m2m.ServiceSub2}
     * @return APIGeneralResponseHolder
     * 
     */
    @WebMethod
    public APIGeneralResponseHolder APIchangeServicePlans(ServiceSub2 serviceSub2)
    {
    	init();
        resetStatusMessage();
        String lApiName = "changeServicePlans";
    	createDBRequestEntry = new CreateDBRequestEntry(logger);

		if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIchangeServicePlans ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** serviceSub2.MDN               :: "+serviceSub2.MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** serviceSub2.Activity          :: "+serviceSub2.activity+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** serviceSub2.PricePlans        :: "+serviceSub2.pricePlans+" **** ****");
        	if( serviceSub2.pricePlans != null ) {
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Traversing Price Plan List **** ****");
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** pricePlan.svcCode::"+serviceSub2.pricePlans.svcCode+" **** **** ****");
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** pricePlan.svcEffDt::"+serviceSub2.pricePlans.svcEffDt+" **** **** ****");
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** pricePlan.svcExpDt::"+serviceSub2.pricePlans.svcExpDt+" **** **** ****");
	        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** Done Traversing Price Plan List **** ****");
        	}
        	if( serviceSub2.oldservices != null ) {
            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** Traversing Old Service List **** **** ****");
	            for( int i = 0; i < serviceSub2.oldservices.length; i++ ) {
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** BEGIN Old Service BEGIN **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** oldService["+i+"].svcCode::"+serviceSub2.oldservices[i].svcCode+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** oldService["+i+"].svcEffDt::"+serviceSub2.oldservices[i].svcEffDt+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** oldService["+i+"].svcExpDt::"+serviceSub2.oldservices[i].svcExpDt+" **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** END Old Service END **** **** ****");            	
	            }
	        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** Done Traversing Old Service List **** **** ****");
        	}
        	if( serviceSub2.services != null ) {
	        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** Traversing New Service List **** **** ****");
	            for( int i = 0; i < serviceSub2.services.length; i++ ) {
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** BEGIN New Service BEGIN **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** newService["+i+"].svcCode::"+serviceSub2.services[i].svcCode+" **** **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** newService["+i+"].svcEffDt::"+serviceSub2.services[i].svcEffDt+" **** **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** **** newService["+i+"].svcExpDt::"+serviceSub2.services[i].svcExpDt+" **** **** **** ****");
	            	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** END New Service END **** **** ****");            	
	            }
	        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** Done Traversing Old Service List **** **** ****");
        	}
		}
		if( activeSprintConnection ) {
			if( !webServiceConnection ) {
	            APIKey apikey  = APIlogin();
	            String key = apikey.getKeyValue();
	        	confirmMsg = new org.omg.CORBA.StringHolder();
		    	try{    
		    		ServiceSub2 validServiceSub2 = serviceSub2;
		    		if( validateOutboundRequest ) {
		    			APIValidationUtil validationUtil = new APIValidationUtil(logger);
		    			validServiceSub2 = validationUtil.validateChangeServicePlansRequest(key, createDBRequestEntry, serviceSub2, resellerV2SubInquiry);
						//Fail safe incase the validation tool fails.
						if( validServiceSub2 == null ) {
							validServiceSub2 = serviceSub2;
						}
		    		}
					if( validServiceSub2 != null ) {
						if( validServiceSub2.oldservices == null && validServiceSub2.services == null ) {
							statusStr = "SUCCEED";
							responseStr = "No changes have occurred with Sprint.";
							aPIGeneralResponseHolder = new APIGeneralResponseHolder(null,responseStr,statusStr);
					        logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Done APIchangeServicePlans :: "+responseStr+" ****");
							return aPIGeneralResponseHolder;
						}
					}
		    	    subMaintenance6.changeServicePlans(key, validServiceSub2, confirmMsg);
			    }catch(invalid_mdn_exception mdn_ex){
					responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
				}catch(invalid_date_exception date_ex){
					responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
				}catch(missing_field_exception field_ex){
					responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
					statusStr ="FAIL";
					responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
				}catch(general_application_error_exception gen_ex){
					responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
			        logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** **** APIchangeServicePlans general_application_error_exception "+gen_ex.errorCode+" **** ****");
			        responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
					statusStr ="FAIL";
				}catch(Exception e){
					responseStr = buildStackTraceErrorResponse(e.getStackTrace());
					statusStr ="FAIL";
					responseStr = "General Exception raised..."+e.getMessage();
				}finally{
					aPIGeneralResponseHolder = 
						  new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
		            APIlogout(apikey);
				}
			} else { //use Sprint's web service Connection
				try {
					ServiceSub2 validServiceSub2 = serviceSub2;
					if( validateOutboundRequest ) {
						APIValidationUtil validationUtil = new APIValidationUtil(logger);
						validServiceSub2 = validationUtil.validateChangeServicePlansRequest(mUserName, mOrderID, createDBRequestEntry, serviceSub2);
						//Fail safe incase the validation tool fails.
						if( validServiceSub2 == null ) {
							validServiceSub2 = serviceSub2;
						}
					}
					if( validServiceSub2 != null ) {
						if( validServiceSub2.oldservices == null && validServiceSub2.services == null && validServiceSub2.pricePlans == null ) {
							statusStr = "SUCCEED";
							responseStr = "No changes have occurred with Sprint.";
							aPIGeneralResponseHolder = new APIGeneralResponseHolder(null,responseStr,statusStr);
					        logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Done APIchangeServicePlans :: "+responseStr+" ****");
							return aPIGeneralResponseHolder;
						}
					}
					aPIGeneralResponseHolder = apiConversionUtil.APIchangeServicePlans(mUserName,mOrderID,validServiceSub2);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIGeneralResponseHolder = new APIGeneralResponseHolder(null, responseStr, api_ex.getStatus());
				}
			}
		} //end true sprint connection
		else {
			responseStr = "Manual execution of changeServicePlans completed";
			statusStr   = "SUCCEED";
			//confirmMsg.value = "Manual completion successful";
			aPIGeneralResponseHolder = 
				  new APIGeneralResponseHolder("Manual completion successful", responseStr, statusStr);
		}
		if( debugMode ) {
          logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIchangeServicePlans ****");
		}
    	createDBRequestEntry = null;
		return aPIGeneralResponseHolder; 
		  
    }
    
    //-------------------------------------------------------------------------------
    
    /**
     * Method was implemented but will probably not be exposed to the webservice
     *        since the changeServicePlans() method provides the same function.
     * @param sub {@link com.spcs.pls.m2m.ServiceSub} 
     * @return APIGeneralResponseHolder
     * 
     */
    @WebMethod
    public APIGeneralResponseHolder APIchangeServices(ServiceSub sub)
    {
    	String lApiName = "changeServices";
        resetStatusMessage();
        APIKey apikey  = APIlogin();
        String key = apikey.getKeyValue();
    	confirmMsg = new org.omg.CORBA.StringHolder();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
    	try{
    	subMaintenance6.changeServices(key, sub, confirmMsg);
    	  }catch(invalid_mdn_exception mdn_ex){
    		  responseStr = mdn_ex.getMessage();
      		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		  }catch(invalid_date_exception date_ex){
			  responseStr = buildStackTraceErrorResponse(date_ex.getStackTrace());
      		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(date_ex.errorCode), lApiName, "invalid_date_exception");
		  }catch(missing_field_exception field_ex){
			  responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
      		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		  }catch(general_application_error_exception gen_ex){
			  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
      		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		  }catch(Exception e){
			  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
      		  statusStr ="FAIL";
		  }finally{
			  aPIGeneralResponseHolder = 
				  new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
                          APIlogout(apikey);
		  }

	    	createDBRequestEntry = null;
		 return aPIGeneralResponseHolder; 
    }
    
  //------------------------------------------------------------------------------
    
    
    /**
     * @param ModifyPortInformation  modifyPortInformation
     * @return APIModifyPortResultResponseHolder
     */
    @WebMethod
    public APIModifyPortResultResponseHolder APImodifyPortInRequest(
    		                           ModifyPortInformation  modifyPortInformation )
    {
    	String lApiName = "modifyPortInRequest";
        resetStatusMessage();
    	modifyPortResult = new ModifyPortResult();
    	modifyPortResultHolder = new ModifyPortResultHolder(modifyPortResult);
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
    	if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APImodifyPortInRequest ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation AccountNo    :: "+modifyPortInformation.accountNumber+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation PortID       :: "+modifyPortInformation.portID+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation MDN          :: "+modifyPortInformation.mdn+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation Reason       :: "+modifyPortInformation.reasonForUpdate+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation Time Zone    :: "+modifyPortInformation.timeZone+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** modifyPortInformation Remarks      :: "+modifyPortInformation.remarks+" **** ****");
    	}
    	if( activeSprintConnection ) {
    		if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	try{
		           // if( modifyPortInformation.reasonForUpdate == null ) {
		            //	modifyPortInformation.reasonForUpdate = "Correcting Customer Information";
		            //}
		    	  subMaintenance6.modifyPortInRequest(key, modifyPortInformation , modifyPortResultHolder);
		    	}catch(invalid_mdn_exception mdn_ex){
	    		  responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
	      		  statusStr ="FAIL";
	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		    	}catch(invalid_time_zone_exception time_ex){
	    		  responseStr = buildStackTraceErrorResponse(time_ex.getStackTrace());
	      		  statusStr ="FAIL";
	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(time_ex.errorCode), lApiName, "invalid_time_zone_exception");
		    	}catch(invalid_value_exception value_ex){
	    		  responseStr = buildStackTraceErrorResponse(value_ex.getStackTrace());
	      		  statusStr ="FAIL";
	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(value_ex.errorCode), lApiName, "invalid_value_exception");
		    	}catch(general_application_error_exception gen_ex){
	    		  logger.log(LOG_EVENT_TYPE, "info", " GENERAL APPLICATION EXCEPTION:: "+gen_ex.errorCode+" "+gen_ex.getMessage());
	    		  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
	      		  statusStr ="FAIL";
	      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
		    	}catch(Exception e){
				  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
	      		  statusStr ="FAIL";
		    	}finally{
			      aPIModifyPortResultResponseHolder = 
			    	  new APIModifyPortResultResponseHolder(modifyPortResultHolder.value,
			    			                                responseStr,
			    				                            statusStr);
	                          APIlogout(apikey);
		    	}
    		} else { //use Sprint's Web Service 
    			try {
    				aPIModifyPortResultResponseHolder = apiConversionUtil.APImodifyPortInRequest(mUserName, mOrderID, modifyPortInformation);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIModifyPortResultResponseHolder = new APIModifyPortResultResponseHolder(null, responseStr, api_ex.getStatus());
    			}
    		}
    	}
        if( debugMode ) {
        	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APImodifyPortInRequest ****");
        }
    	createDBRequestEntry = null;
    	return aPIModifyPortResultResponseHolder;
    }
    
    
 //-----------------------------------------------------------------------------------
    /**
     * @param MDN String Representing the MDN to be ported out
     * @param cancelRemarks String of text explaining the Reason for cancelling the port
     * 
     * @return APIPortCancelResponseHolder
     */
    @WebMethod 
    public APIPortCancelResponseHolder APIportCancel(String mdn, String cancelRemarks)
    {
    	String lApiName = "portCancel";
    	init();
        resetStatusMessage();
    	CancelPortResultHolder cancelPortResultHolder = new CancelPortResultHolder();    	
    	CancelPortInformation  cancelPortInfo = new CancelPortInformation ();
    	APIPortCancelResponseHolder aPIPortCancelResponseHolder;
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIportCancel ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String MDN        :: "+mdn+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String CancelRmks :: "+cancelRemarks+" **** ****");
        }
    	if( activeSprintConnection ) {
    		
    		if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	cancelPortInfo.remark = cancelRemarks;
		    	
		    	try{
		    		 subMaintenance6.portCancel(key, mdn, cancelPortInfo, cancelPortResultHolder);
		    	}catch(invalid_mdn_exception mdn_ex){
		    		  responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		      		  statusStr ="FAIL";
		    	}catch(general_application_error_exception gen_ex){
		    		  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		      		  statusStr ="FAIL";
		    	}catch(Exception e){
		    		  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      		  statusStr ="FAIL";
		    	}finally{
		    		aPIPortCancelResponseHolder = 
		    			new APIPortCancelResponseHolder(cancelPortResultHolder.value, responseStr,statusStr );
		                          APIlogout(apikey);
		    	} 
    		} else { // use Sprint's web service 
    			try {
    				aPIPortCancelResponseHolder = apiConversionUtil.APIportCancel(mUserName, mOrderID, mdn, cancelRemarks);
        		} catch ( SprintAPIException api_ex ) {
        			logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** SprintAPIException thrown in the APIportCancel method ****");
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** Exception thrown in the APIportCancel method ****");
        				responseStr = api_ex.getMessage();
        			}
        			aPIPortCancelResponseHolder = new APIPortCancelResponseHolder(null, responseStr, api_ex.getStatus());
    			}
    		}
    		
    	} else { //fake response
    		CancelPortResult cancel = new CancelPortResult("Cancel Status","Cancel Status Text");
    		aPIPortCancelResponseHolder = new APIPortCancelResponseHolder(cancel,"Simulated cancellation of port","SUCCEED");
    	}
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIportCancel ****");
        }
    	createDBRequestEntry = null;
    	return aPIPortCancelResponseHolder;
    }
    
    //-------------------------------------------------------------------------------
    
    /**
     * @param String MDN (required)
     * @return APIGeneralResponseHolder
     * @throws InterruptedException 
     */
    @WebMethod
    public APIGeneralResponseHolder APIsuspendSubscription(String MDN, String ACTIVITY) throws InterruptedException
    {
    	init();
    	String lApiName = "suspendSubscription";
        resetStatusMessage();
        createDBRequestEntry = new CreateDBRequestEntry(logger);
        if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIsuspendSubscription ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String MDN        :: "+MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** String ACTIVITY   :: "+ACTIVITY+" **** ****");
        }
        if( activeSprintConnection ) {
        	if( !webServiceConnection ) {
		        APIKey apikey  = APIlogin();
		        String key = apikey.getKeyValue();
		    	activity = null;
		    	confirmMsg = new org.omg.CORBA.StringHolder();
		    	try{
		    		if( validateOutboundRequest ) {
		    			APIValidationUtil validationUtil = new APIValidationUtil(logger);
		    			String suspendActivity = "";
		    			if( ACTIVITY == null ) {
		    				suspendActivity = APIValidationUtil.MODIFY_SUB_SUSPEND;
		    			} else {
		    				suspendActivity = ACTIVITY;
		    			}
		    			APIGeneralResponseHolder validationResponse = validationUtil.validateSubscriptionModificationRequest(key, createDBRequestEntry, MDN, suspendActivity, resellerV2SubInquiry);
		    			if( validationResponse != null ) {
		    				if( validationResponse.statusMessage.equals(APIValidationUtil.RESPONSE_STATUS_SUCCEED) ) {
			    				subMaintenance6.suspendSubscription(key, MDN, ACTIVITY, confirmMsg);
		    				} else {
		    					confirmMsg.value = validationResponse.responseMessage;
		    				}
		    			} else {
		    				subMaintenance6.suspendSubscription(key, MDN, ACTIVITY, confirmMsg);
		    			}
		    		} else { //do not validate the outbound request
		    			subMaintenance6.suspendSubscription(key, MDN, ACTIVITY, confirmMsg);
		    		}
			    }catch(invalid_mdn_exception mdn_ex){
			    	  responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		      		  statusStr ="FAIL";
		      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
			    }catch(missing_field_exception field_ex){
			    	  responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		      		  statusStr ="FAIL";
		      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
			    }catch(general_application_error_exception gen_ex){
			    	  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		      		  statusStr ="FAIL";
		      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
			    }catch(Exception e){
			    	  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		      		  statusStr ="FAIL";
			    }finally{
		              try{
		                     createDBRequestEntry.logDBchangeSubscription(ACTIVITY, MDN, null, APIHelper.suspendSubscription, confirmMsg.value);
		              }catch(Exception e){
		                    responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		              }
					  aPIGeneralResponseHolder = 
						  new APIGeneralResponseHolder(confirmMsg.value, responseStr, statusStr);
		                          APIlogout(apikey);
			
			    }
        	} else { //use Sprint web service
        		try {      				
        			
            			suspendPricePlanCheck(ACTIVITY,MDN);           				
    			    	
        				waiting();
        				
        				String statusCheck = suspendSubscriptionCheck(MDN);
        				
        				if(statusCheck != null && statusCheck.equals("A")){
        				
        					if( validateOutboundRequest ) {
        						APIValidationUtil validationUtil = new APIValidationUtil(logger);
        						String modifyActivity = "";
        						if( APIValidationUtil.MODIFY_SUB_HOTLINE.equalsIgnoreCase(ACTIVITY) ) {
        							modifyActivity = ACTIVITY;
        						} else {
        							modifyActivity = APIValidationUtil.MODIFY_SUB_SUSPEND;
        						}
        						APIGeneralResponseHolder validationResponse = validationUtil.validateSubscriptionModificationRequest(mUserName, mOrderID, MDN, modifyActivity, createDBRequestEntry);
        						if( validationResponse != null ) {
        							if( validationResponse.statusMessage.equals(APIValidationUtil.RESPONSE_STATUS_SUCCEED) ) {
        								aPIGeneralResponseHolder = apiConversionUtil.APIsuspendSubscription(mUserName, mOrderID, MDN, ACTIVITY);
        							} else {
        								aPIGeneralResponseHolder = validationResponse;
        								aPIGeneralResponseHolder.statusMessage = APIValidationUtil.RESPONSE_STATUS_SUCCEED;
        								aPIGeneralResponseHolder.responseMessage = validationResponse.responseMessage;
        							}
        						} else {
        							aPIGeneralResponseHolder = apiConversionUtil.APIsuspendSubscription(mUserName, mOrderID, MDN, ACTIVITY);
        						}
        					} else {
        						aPIGeneralResponseHolder = apiConversionUtil.APIsuspendSubscription(mUserName, mOrderID, MDN, ACTIVITY);
        					}
        				} else {
        					
        					if(statusCheck == null){
        						statusCheck = "null";
        					}        					
        					aPIGeneralResponseHolder.statusMessage = APIValidationUtil.RESPONSE_STATUS_SUCCEED;
        					aPIGeneralResponseHolder.responseMessage = "The Switch Status returned as [ "+statusCheck+" ] "; 
        					aPIGeneralResponseHolder.apiResponseMessage = "";
        				}
        				
        				logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Status Message        :: "+aPIGeneralResponseHolder.statusMessage+" **** ] ****");
            			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Response Message        :: "+aPIGeneralResponseHolder.responseMessage+" **** ] ****");
            			logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** API Response Message        :: "+aPIGeneralResponseHolder.apiResponseMessage+" **** ] ****");
            			
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPIGeneralResponseHolder = new APIGeneralResponseHolder(null,responseStr,api_ex.getStatus());
        		}
        	}
        } else {
     	   aPIGeneralResponseHolder = new APIGeneralResponseHolder("SUCCEED","Manually bypassed restore process","SUCCEED");
        }
	    if( debugMode ) {
	    	logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIsuspendSubscription ****");
	    }
    	createDBRequestEntry = null;
    	
    	//RETURNED STATUS MESSAGES
        //logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return Status Message        :: "+aPIGeneralResponseHolder.statusMessage+" **** ] ****");
		//logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return Response Message        :: "+aPIGeneralResponseHolder.responseMessage+" **** ] ****");
		//logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** [ **** Return API Response Message        :: "+aPIGeneralResponseHolder.apiResponseMessage+" **** ] ****");
	    return aPIGeneralResponseHolder;
    }
    
    
    //-------------------------------------------------------------------------------
    
    /**
     * Provides functionality to Bind an already active MDN to a new ESN and deassociate it with the Old Esn ( not passed to the API )
     * 
     * @param  MDN String access number associated to the subscription
     * @param  newESNorMEID String valid ESN or MEID in either decimal or hex format to which the Reseller subscription needs to be swapped to
     * 
     * @return APISwapESNResponseHolder
     */
    @WebMethod
    public APISwapESNResponseHolder APIswapESN(String MDN, String newESNorMEID)
    {
    	String lApiName = "swapESN";
    	init();
        resetStatusMessage();
       	MSID   = new org.omg.CORBA.StringHolder(); 
       	confirmMsg = new org.omg.CORBA.StringHolder();
    	createDBRequestEntry = new CreateDBRequestEntry(logger);
       	if( debugMode ) {
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Initiated APIswapESN ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** MDN                :: "+MDN+" **** ****");
            logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** newESNorMEID       :: "+newESNorMEID+" **** ****");
       	}
       	if( activeSprintConnection ) {
       		if( !webServiceConnection ) {
	            APIKey apikey  = APIlogin();
	            String key = apikey.getKeyValue();
		       	try{ 
		    	    subMaintenance6.swapESN(key, MDN, newESNorMEID, activity, MSID, confirmMsg);
		          }catch(invalid_mdn_exception mdn_ex){
		        	  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "INVALID MDN EXCEPTION :: ERRORCODE:"+mdn_ex.errorCode);
				     responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
		  		     statusStr ="FAIL";
		     		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
		          }catch(invalid_esn_exception esn_ex){
		        	  logger.log(LOG_EVENT_TYPE, LOG_ERROR, "INVALID ESN EXCEPTION :: ERRORCODE:"+esn_ex.errorCode);
		        	  responseStr = buildStackTraceErrorResponse(esn_ex.getStackTrace());
		   		     statusStr ="FAIL";
		     		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(esn_ex.errorCode), lApiName, "invalid_esn_exception");
		          }catch(missing_field_exception field_ex){
		        	  responseStr = buildStackTraceErrorResponse(field_ex.getStackTrace());
		   		     statusStr ="FAIL";
		     		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(field_ex.errorCode), lApiName, "missing_field_exception");
		          }catch(general_application_error_exception gen_ex){
		        	  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
		   		     statusStr ="FAIL";
		     		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
			      }catch(Exception e){
			    	  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
		  		      statusStr ="FAIL";
			      }finally
			      {
			    	  aPISwapESNResponseHolder = new
			    	        APISwapESNResponseHolder(MSID.value, confirmMsg.value, responseStr, statusStr);
		                        APIlogout(apikey);
					
			      }
       		} else { //use Sprint web service
       			try {
       				aPISwapESNResponseHolder = apiConversionUtil.APIswapESN(mUserName, mOrderID, MDN, newESNorMEID);
        		} catch ( SprintAPIException api_ex ) {
        			try {
        				responseStr = createDBRequestEntry.getAPIErrorMessage(api_ex.getWsError().getProviderError().getProviderErrorCode(), lApiName, api_ex.getWsError().getProviderError().getProviderErrorText());
        			} catch( Exception e ) {
        				responseStr = api_ex.getMessage();
        			}
        			aPISwapESNResponseHolder = new APISwapESNResponseHolder(null,null,responseStr,api_ex.getStatus());
       			}
       		}
       	} else { //no active connection with Sprint
       		aPISwapESNResponseHolder = new APISwapESNResponseHolder("000008675309", "SUCCEED","Manually bypassed restore process","SUCCEED");
       	}
       	if( debugMode ) {
       		logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIswapESN ****");
       	}
    	createDBRequestEntry = null;
	    return aPISwapESNResponseHolder;
    }
    //--------------------------------------------------------------------------------
    /**
     * String newMDNIn is optional, but if not provided Sprint will assign an MDN
     *        for us.  
     * <p>[5/25/2007] [dta] From testing/discussion with Subra, we cannot populate the newMDNIn field.</p>
     *        
     * @param oldMDN String 
     * @param newMDNIn String 
     * @return APISwapMDNResponseHolder
     *  
     */
    @WebMethod
    public APISwapMDNResponseHolder APIswapMDN(String oldMDN, String newMDNIn)
    {
    	String lApiName = "swapMDN";
            resetStatusMessage(); 
            APIKey apikey  = APIlogin();
            String key = apikey.getKeyValue();
    	activity    = null;
    	newMDN      = new org.omg.CORBA.StringHolder(newMDNIn);
       	MSID        = new org.omg.CORBA.StringHolder(); 
    	confirmMsg  = new org.omg.CORBA.StringHolder();        	
    	
    	try{
	        logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** BEGIN APIswapMDN ****");
	          logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** oldMDN    :: "+oldMDN+" **** ****");
	          logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** newMDN    :: "+newMDNIn+" **** ****");
    	    subMaintenance6.swapMDN(key, oldMDN, activity, newMDN, MSID, confirmMsg);	
          }catch(invalid_mdn_exception mdn_ex){
  		  responseStr = buildStackTraceErrorResponse(mdn_ex.getStackTrace());
          logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** **** **** Exception errorCode    :: "+mdn_ex.errorCode+" **** ****");
    		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(mdn_ex.errorCode), lApiName, "invalid_mdn_exception");
	      }catch(general_application_error_exception gen_ex){
			  responseStr = buildStackTraceErrorResponse(gen_ex.getStackTrace());
    		  statusStr ="FAIL";
      		  responseStr = createDBRequestEntry.getAPIErrorMessage(Integer.toString(gen_ex.errorCode), lApiName, "general_application_error_exception");
	      }catch(Exception e){
	    	  responseStr = buildStackTraceErrorResponse(e.getStackTrace());
    		  statusStr ="FAIL";
	      }finally{
	    	  aPISwapMDNResponseHolder = new 
	    	       APISwapMDNResponseHolder(newMDN.value, 
	    	    		                    MSID.value, 
	    	    		                    confirmMsg.value,
	    	    		                    responseStr,
	    	    		                    statusStr);
                    APIlogout(apikey);
	      }

          logger.log(LOG_EVENT_TYPE, LOG_INFO, "**** Done APIswapMDN ****");
	      return aPISwapMDNResponseHolder;
    }

      
	/**
	 * Basic function used to check if a String is null..
	 * 
	 * <p>True if param is null or empty otherwise false.</p>
	 * 
	 * @param param String value that will be validated. 
	 * 
	 * @return boolean representing if the Object is deemed as null or not.
	 */
	private boolean isNull(String param)
	{
              if(param == null)
              {
                return true;
              } 
              else
              {
		if(param.trim().length() == 0)
                {
			return true;
		}
              }
           return false;
	}
	
	/*
	private boolean isObjectNull(java.lang.Object o)
        {
		return true;
	}
	*/

	
	@WebMethod(exclude = true)
	// The wait time between WebMethod calls
	private static void waiting(){
		try{
			// waiting for 1 second works
			Thread.sleep(100); // do nothing for 100 ms
		} catch(InterruptedException e) {
				e.printStackTrace();
		}
	}
	
	
	@WebMethod(exclude = true)
	// Code to change SOC code when a RestoreSubscription request is received.
	private void restorePricePlanCheck(String MDN){
		try{
			if(aPIGeneralResponseHolder != null){
					
						if(aPIGeneralResponseHolder.statusMessage.equals("SUCCEED")){ 			
			
							APIResellerSubInquiryResponseHolder holder = APIresellerV2SubInquiry(null, MDN);
							if (holder != null){
								if(holder.responseMessage.trim().equals("") && holder.statusMessage.equals("SUCCEED") &&
    								holder.getPricePlanList() != null ){
   
										PLSSali[] pricePlanList = holder.getPricePlanList().value;
										String svcCode = pricePlanList[0].svcCode;
										
										if(svcCode != null){
											if(!svcCode.equals("PRSCARD3") && !svcCode.equals("PRSCARD5") && !svcCode.equals(RESTORE_SVC_CODE) ){
												Sali2 subPricePlan = new Sali2(RESTORE_SVC_CODE,null,null,null);
												ServiceSub2 sub = new ServiceSub2(null,MDN,subPricePlan,null,null);
												APIGeneralResponseHolder SOCchangeGeneralResponseHolder = APIchangeServicePlans(sub);
											}
										}
    							}
							}	

						}
			
				}
			} catch (NullPointerException e){
			
			}
		
	}
	
	
	@WebMethod(exclude = true)
	// Code to change SOC code when a SuspendSubscription request is received.
	private void suspendPricePlanCheck(String ACTIVITY, String MDN){
		try{
			APIResellerSubInquiryResponseHolder holder = APIresellerV2SubInquiry(null, MDN);
			if (holder != null){
				if(holder.responseMessage.trim().equals("") && holder.statusMessage.equals("SUCCEED") &&
						holder.getPricePlanList() != null ){

						PLSSali[] pricePlanList = holder.getPricePlanList().value;
						String svcCode = pricePlanList[0].svcCode;
						
						if(holder.getAccessEqpAsgmList() != null){
							AccessNbrAsgmInfo[] accessNbrAsgmList = holder.getAccessNbrAsgmList().value;
							String switchStatus = accessNbrAsgmList[0].switchStatusCd;
						
							if(svcCode != null && switchStatus.equals("A")){
								if(!svcCode.equals("PRSCARD3") && !svcCode.equals("PRSCARD5") && !svcCode.equals(SUSPEND_SVC_CODE)){
									Sali2 subPricePlan = new Sali2(SUSPEND_SVC_CODE,null,null,null);
									ServiceSub2 sub = new ServiceSub2(ACTIVITY,MDN,subPricePlan,null,null);
									APIGeneralResponseHolder SOCchangeGeneralResponseHolder = APIchangeServicePlans(sub);
								}
							}
			
						}
				}
			}
		} catch (NullPointerException e){
	
		}
	}
	
	
	@WebMethod(exclude = true)
	// Code to check SwitchStatus of an MDN when a SuspendSubscription request is received.
	private String suspendSubscriptionCheck(String MDN){
		try{

			APIResellerSubInquiryResponseHolder holder = APIresellerV2SubInquiry(null, MDN);
			if (holder != null){
				if(holder.responseMessage.trim().equals("") && holder.statusMessage.equals("SUCCEED") &&
						holder.getPricePlanList() != null ){

						PLSSali[] pricePlanList = holder.getPricePlanList().value;
						String svcCode = pricePlanList[0].svcCode;
						
						if(holder.getAccessEqpAsgmList() != null){
							AccessNbrAsgmInfo[] accessNbrAsgmList = holder.getAccessNbrAsgmList().value;
							String switchStatus = accessNbrAsgmList[0].switchStatusCd;
						
							return switchStatus;
			
						}
				}
			}
		} catch (NullPointerException e){
	
		}
		
		return null;
	}
	
	
	private String buildStackTraceErrorResponse(StackTraceElement []stackTraceElement )
	{
		String stackTrace =null;
		/*for( int i = 0; i < stackTraceElement.length; i++ ) {
	        logger.log(LOG_EVENT_TYPE, LOG_ERROR, "Exception : \n" + stackTraceElement[i]);			
		}*/
        stackTrace = APIHelper.buildStackTraceErrorResponse(stackTraceElement);
        logger.log(LOG_EVENT_TYPE, LOG_ERROR, "Exception : \n" + stackTrace);
	   return stackTrace;
	}
	
	
	@WebMethod
	public PendingSubscriptionNPA getNewMDN(PendingSubscriptionNPAHolder iPendingSubscriptionNPAHolder ) {
		PendingSubscriptionNPA retValue = new PendingSubscriptionNPA();
		try {
	   	    initialcontext = new InitialContext();
	   	    k11DataSource  = (DataSource)initialcontext.lookup(k11DbName);
	   	    Connection kenanConn = k11DataSource.getConnection(); 
			StoredProc sp  = new StoredProc(kenanConn,"api3Service");
			SPArgs spargs  = new SPArgs();
			spargs.put("sp", "SP_MVNO_GET_FAKE_MDN");
			spargs.put("arg1", iPendingSubscriptionNPAHolder.value.NPA);
			ResultSet rs   = sp.exec(spargs);
			try{ 
				rs.next();
				if( rs.getString("STATUS").equals("SUCCEED") ) {
					retValue.MDN 			= rs.getString("MDN");
					retValue.MSID			= rs.getString("MSID");
					retValue.effectiveDate	= rs.getString("chgdate");
				} else {
					logger.log(LOG_EVENT_TYPE, LOG_ERROR, "**** pendingSubscriptionNPAHolder properties: ****");
					retValue.MDN 			= rs.getString("MDN");
					retValue.MSID			= rs.getString("MSID");
					retValue.effectiveDate	= rs.getString("chgdate");
				}
			} catch ( SQLException sql_ex ) {
				
			} finally {
				sp.close(rs);
				kenanConn.close();
			}
		} catch (Exception gen_ex ) {
			
		}
		return retValue;
	}

       //public static void main(String [] args)
       //{
       //   API3 api3 = new API3(); 
       //   api3.initCorbInterface();
       //}	
		
}//end of API
