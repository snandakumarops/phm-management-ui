package com.redhat.phmprocess.ui;/*
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




import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

public class PHMUIVerticle extends AbstractVerticle {

	public static final String BASIC_AUTH="Basic cGFtQWRtaW46cmVkaGF0cGFtMSE=";


	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		PHMUIVerticle vertxWebUI = new PHMUIVerticle();
		vertxWebUI.start();
	}


	static Vertx vertx = Vertx.vertx();



	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		router.route("/static/*").handler(StaticHandler.create("webroot"));

		router.get("/getTasksDashboard/:userId").handler(this::getTaskDashboard);

		router.get("/getTaskDetails/:taskId").handler(this::getTaskDetails);

		router.post("/approve/:taskId/:userId/:close/:processInstanceId").handler(this::approve);


		vertx.createHttpServer().requestHandler(router).listen(8037);
	}

	//Approve Changes
	private void approve(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			String userId = routingContext.request().getParam("userId");
			String close = routingContext.request().getParam("close");
			String processInstanceId = routingContext.request().getParam("processInstanceId");
			TaskHandlerUtil.approveOrReject(taskId,userId,routingContext.getBodyAsString(),close,processInstanceId);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(Json.encodePrettily("Approved"));

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	//Get Task Dashboard
	private void getTaskDashboard(RoutingContext routingContext) {
		String userId = routingContext.request().getParam("userId");
		try {
			List<TaskSummaryObject> taskSummary = TaskHandlerUtil.fetchDashboardData(userId);
			System.out.println(taskSummary);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(taskSummary));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Get Task Details
	private void getTaskDetails(RoutingContext routingContext) {
		try {
			String taskId = routingContext.request().getParam("taskId");
			Map taskSummary = TaskHandlerUtil.getTaskSummary(taskId,BASIC_AUTH);
			System.out.println(taskSummary);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(taskSummary));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}