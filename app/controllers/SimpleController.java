package controllers;

import play.mvc.Controller;
import play.mvc.With;

import com.plumgine.play.Breadcrumb;
import com.plumgine.play.BreadcrumbGenerator;
import com.plumgine.play.BreadcrumbIgnore;


@With({BreadcrumbGenerator.class})
public class SimpleController extends Controller {
	
	
	// first level breadcrumb
	@Breadcrumb("Do Something")
	public static void doSomething() {
		
	}
	
	// second level breadcrumb
	@Breadcrumb(value="Something Else", level=1)
	public static void doSomethingElse() {
		
	}
	
	// do not capture this method in breadcrumb
	@BreadcrumbIgnore
	public static void nothingImportant() {
		
	}	
	

	
	
}
