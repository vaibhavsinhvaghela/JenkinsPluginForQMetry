package org.jenkinsci.plugins.test1;

import hudson.Extension;
import hudson.model.*;

@Extension
public class OnlineRootAction implements RootAction{

	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return "clipboard.png";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "Create example";
	}

	@Override
	public String getUrlName() {
		// TODO Auto-generated method stub
		return "http://google.com";
	}

}
