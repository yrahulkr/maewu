package com.crawljax.web.jaxrs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crawljax.web.fs.WorkDirManager;
import com.crawljax.web.model.CrawlRecord;
import com.crawljax.web.model.CrawlRecords;
import com.crawljax.web.model.DiffImages;
import com.crawljax.web.model.TestImageDiff;
import com.crawljax.web.model.TestImages;
import com.crawljax.web.model.TestRecord;
import com.crawljax.web.model.TestRecords;
import com.crawljax.web.runner.TestRunner;
import com.crawljax.web.utils.ImageUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/tests")
public class TestResource {

	private final CrawlRecords crawlRecords;
	private final TestRecords testRecords;
	private final WorkDirManager workDirManager;
	private final TestRunner testRunner;

	@Inject
	TestResource(CrawlRecords crawlRecords, TestRecords testRecords, 
			WorkDirManager workDirManager, TestRunner testRunner) {
		this.crawlRecords = crawlRecords;
		this.testRecords = testRecords;
		this.workDirManager = workDirManager;
		this.testRunner = testRunner;
	}

	@GET
	public Response getHistory(@QueryParam("active") Boolean active) {
		List<TestRecord> list;
		list = testRecords.getTestList();
		return Response.ok(list).build();
	}

	@POST
	public Response addTestRecord() {
		CrawlRecord currentCrawl = crawlRecords.getCurrentCrawlRecord();
		TestRecord record = testRecords.add(currentCrawl.getOutputFolder());
		testRunner.queue(record);
		return Response.ok(record).build();
	}

	@GET
	@Path("{testId}")
	public Response getTest(@PathParam("testId") int id) {
		Response r;
		TestRecord record = testRecords.findByID(id);
		if (record != null)
			r = Response.ok(record).build();
		else
			r = Response.serverError().build();
		return r;
	}


	
	@GET
	@Path("/imageData")
	@Produces({"images/gif", "images/png", "images/jpg", MediaType.APPLICATION_JSON})
	public Response getTestImageData(@QueryParam("stateName") String stateName, @QueryParam("testId") int testId) {
		Response r;
		TestRecord record = testRecords.findByID(testId);
		TestImageDiff currentDiff = null;
		for(TestImageDiff diff: record.getDiffs()) {
			if(diff.getState().equalsIgnoreCase(stateName))
				currentDiff = diff;
		}
		
		if(currentDiff == null) {
			r = Response.ok("null").build();
		}
		else {
			File oldImageFile = new File(currentDiff.getOldScreenShot());
			File newImageFile = new File(currentDiff.getNewScreenShot());
			if(!oldImageFile.exists() || !newImageFile.exists())
				r= Response.serverError().build();
			else {
				try {
					// Put the old and new images side by side to be displayed. 
					BufferedImage oldImage = ImageIO.read(oldImageFile);
					BufferedImage newImage = ImageIO.read(newImageFile);
					BufferedImage finalImage = ImageUtils.joinBufferedImage(oldImage, newImage);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();

				    ImageIO.write(finalImage, "png", baos);
				    String imageData  = Base64.getEncoder().encodeToString(baos.toByteArray());
	
				    r= Response.ok(imageData).build();
	
				    
				} catch (IOException e) {
					e.printStackTrace();
					r = Response.serverError().build();
				}

			
			}
		}
		return r;
	}

	
	@GET
	@Path("/allImagesOfTest")
	public Response getTestImages(@QueryParam("testId") int testId) {
		
		Response r;
		
		TestRecord record = testRecords.findByID(testId);
		TestImages testImages = new TestImages();
		testImages.testId = testId;
		if (testImages.diffImages == null)
			testImages.diffImages = new ArrayList<DiffImages>();
		
		for(TestImageDiff diff: record.getDiffs()) {
			DiffImages diffImages = new DiffImages();
			testImages.diffImages.add(diffImages);
			diffImages.setStateName(diff.getState());
			File oldImageFile = new File(diff.getOldScreenShot());
			File newImageFile = new File(diff.getNewScreenShot());
			if(!oldImageFile.exists() || !newImageFile.exists()) {
				r= Response.serverError().build();
				return r;
			}
			
			try {
				BufferedImage oldImageBuff = ImageIO.read(oldImageFile);
				BufferedImage newImageBuff = ImageIO.read(newImageFile);
				
				ByteArrayOutputStream oldbaos = new ByteArrayOutputStream();
			    ImageIO.write(oldImageBuff, "png", oldbaos);
			    String oldImage  = Base64.getEncoder().encodeToString(oldbaos.toByteArray());
			    
			    ByteArrayOutputStream newbaos = new ByteArrayOutputStream();
			    ImageIO.write(oldImageBuff, "png", newbaos);
			    String newImage  = Base64.getEncoder().encodeToString(newbaos.toByteArray());
				
			    diffImages.setNewImage(newImage);
				diffImages.setOldImage(oldImage);
			}catch(IOException ex) {
				r = Response.serverError().build();
				return r;
			}
		}
		
		r = Response.ok(testImages).build();
		return r;
		
	}
}
