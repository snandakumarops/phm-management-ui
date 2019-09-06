package com.redhat.makerchecker.ui;/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */



import com.RulesFired;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

public class MakerCheckerVerticle extends AbstractVerticle {

	public static final String BASIC_AUTH="Basic cGFtQWRtaW46cmVkaGF0cGFtMSE=";


	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		MakerCheckerVerticle vertxWebUI = new MakerCheckerVerticle();
		vertxWebUI.start();
	}


	static Vertx vertx = Vertx.vertx();



	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		router.get("/getTasksDashboard").handler(this::getTaskDashboard);

		router.get("/getSimulationDashboardForBaseList/:taskId").handler(this::getSimulationDashboardForBaseList);


		router.get("/getSimulationDashboardForSimulationList/:taskId").handler(this::getSimulationDashboardForSimulationList);

		router.route("/static/*").handler(StaticHandler.create("webroot"));

		router.post("/approve/:taskId").handler(this::approve);

		router.post("/sendToChecker/:artifactName/:authorComments").handler(this::sendToChecker);


		router.post("/reject/:taskId").handler(this::reject);

		router.get("/loadSvg/:instanceId/:containerId").handler(this::loadSvg);


		vertx.createHttpServer().requestHandler(router).listen(8037);
	}

	//Checker Approve Changes
	private void approve(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			TaskHandlerUtil.approveOrReject(true, taskId,BASIC_AUTH);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(Json.encodePrettily("The Changes will now be promoted."));

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	//Send to Checker for review
	private void sendToChecker(RoutingContext routingContext) {
		try {
			String artifactName = routingContext.request().getParam("artifactName");

			String comments = routingContext.request().getParam("authorComments");

			BuildAndCheckerReview.initiateMakerCheckerWorkflow(artifactName, comments,BASIC_AUTH);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(Json.encodePrettily("Changes will be sent to the checker for review."));



		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	//Checker Reject
	private void reject(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			TaskHandlerUtil.approveOrReject(false, taskId,BASIC_AUTH);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(Json.encodePrettily("The changes have been sent back to the Author"));

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

		//Simulation list - before
		private void getSimulationDashboardForBaseList(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			List<RulesFired> taskSummary = TaskHandlerUtil.fetchSimulation("baseList",taskId,BASIC_AUTH);
			System.out.println(taskSummary);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(taskSummary));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Load process diagram showing inflight instances
	private void loadSvg(RoutingContext routingContext) {
		try {
			String instanceId = routingContext.request().getParam("instanceId");
			String containerId = routingContext.request().getParam("containerId");
			LoadSvg.loadSvg(instanceId,containerId,BASIC_AUTH);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily("success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Simulation list - after
	private void getSimulationDashboardForSimulationList(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			List<RulesFired> taskSummary = TaskHandlerUtil.fetchSimulation("simulationList",taskId,BASIC_AUTH);
			System.out.println(taskSummary);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(taskSummary));


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Checker Review tasks
	private void getTaskDashboard(RoutingContext routingContext) {

		try {
			List<TaskSummaryObject> taskSummary = TaskHandlerUtil.fetchDashboardData(BASIC_AUTH);
			System.out.println(taskSummary);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(taskSummary));

		} catch (Exception e) {
			e.printStackTrace();
		}





	}






}