package org.jenkinsci.plugins.test1;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import hudson.model.AbstractProject;
//import hudson.model.Hudson;
import hudson.tasks.Builder;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Recorder;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link TextExamplePublisher} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class TextExamplePublisher extends Recorder implements SimpleBuildStep {
	
	 private String name;
     private String version;
     private String apikey;
     private String qtm4jurl;
     private String file;
     private String testrunname;
     private String labels;
     private String sprint;
     private String component;
     private static String selection;
     
     public static String getSelection(JSONObject obj){
    	 return selection;
     }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}



	public String getApikey() {
		return apikey;
	}



	public void setApikey(String apikey) {
		this.apikey = apikey;
	}



	public String getQtm4jurl() {
		return qtm4jurl;
	}



	public void setQtm4jurl(String qtm4jurl) {
		this.qtm4jurl = qtm4jurl;
	}



	public String getFile() {
		return file;
	}



	public void setFile(String file) {
		this.file = file;
	}



	public String getTestrunname() {
		return testrunname;
	}



	public void setTestrunname(String testrunname) {
		this.testrunname = testrunname;
	}



	public String getLabels() {
		return labels;
	}



	public void setLabels(String labels) {
		this.labels = labels;
	}



	public String getSprint() {
		return sprint;
	}



	public void setSprint(String sprint) {
		this.sprint = sprint;
	}



	public String getComponent() {
		return component;
	}



	public void setComponent(String component) {
		this.component = component;
	}



	public TextExamplePublisher(){
		
	}
    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public TextExamplePublisher(String name,String apikey, String qtm4jurl, String file, String testrunname, String labels, String sprint, String version, String component) {
        this.version = name;
        this.apikey=apikey;
        this.qtm4jurl=qtm4jurl;
        this.file=file;
        this.testrunname=testrunname;
        this.labels=labels;
        this.sprint=sprint;
        this.component=component;
        this.version=version;
    }

    /**
     * We'll use this from the {@code config.jelly}.
     * @throws IOException 
     */
 

    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws IOException {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.
        // This also shows how you can consult the global configuration of the builder
    	UploadReport ur;
    	//----------------------------------------------------------------------------
    	//----------------------------------------------------------------------------
    	try {
			//con.sendingPostRequest();
    		URL url = new URL(this.getQtm4jurl());
    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    		connection.setRequestMethod("POST");
    		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    		connection.setDoInput(true);
    		connection.setDoOutput(true);

    		StringBuilder jsonBody = new StringBuilder("{");
    		jsonBody.append("\"format\":" + "\"testng/xml\",");
    		jsonBody.append("\"testRunName\":" + "\""+this.getTestrunname()+"\",");
    		jsonBody.append("\"apiKey\":" + "\""+this.getApikey()+"\"");
    		jsonBody.append("}");

    		OutputStream os = connection.getOutputStream();
    		os.write(jsonBody.toString().getBytes("UTF-8"));
    		InputStream fis = connection.getInputStream();

    		StringWriter response = new StringWriter();
    		String encoding = "UTF-8";
    		IOUtils.copy(fis, response, encoding);
    		System.out.println(response.toString());
    		System.out.println(UploadReport.uploadToS3(response.toString(),this.getFile()));
    		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link TextExamplePublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/hudson/plugins/hello_world/TextExamplePublisher/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use {@code transient}.
         */
        private boolean useFrench;
        
        public ListBoxModel doFillSelectionItems(@QueryParameter String selection)
        {
            return new ListBoxModel(
            		new Option("Cucumber/JSON","cucumber/json", selection.matches("json")),
                    new Option("JUnit/XML", "junit/xml", selection.matches("xml")),
                    new Option("qas/JSON", "qas/json", selection.matches("xml")),
                    new Option("testng/XML", "testng/xml", selection.matches("xml"))
                   // new Option("Red", "ff0000", false)
                    );
        }
        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
       /* public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }*/
        
        public FormValidation doCheckApikey(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("API Key");
            return FormValidation.ok();
        }
        
        public FormValidation doCheckTestrunname(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please give 'Test run name'");
            return FormValidation.ok();
        }
        
        public FormValidation doCheckQtm4jurl(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please give 'Test run name'");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Integrate with QMetry test manager for JIRA";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useFrench = formData.getBoolean("useFrench");
            selection = formData.getString("selection");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }

        /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbooks by the naming convention.
         */
        public boolean getUseFrench() {
            return useFrench;
        }
    }

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return BuildStepMonitor.NONE;
	}
}

