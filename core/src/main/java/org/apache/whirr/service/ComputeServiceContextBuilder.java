/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.whirr.service;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.ssh.jsch.config.JschSshClientModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A convenience class for building jclouds {@link ComputeServiceContext} objects.
 */
public class ComputeServiceContextBuilder {
  private static final Logger LOG =
    LoggerFactory.getLogger(ComputeServiceContextBuilder.class);
   
  public static ComputeServiceContext build(ClusterSpec spec) throws IOException {
    Configuration jcloudsConfig =
      spec.getConfigurationForKeysWithPrefix("jclouds");
    Set<AbstractModule> wiring = ImmutableSet.of(new JschSshClientModule(),
      new Log4JLoggingModule());
    if (spec.getProvider().equals("ec2")){
      LOG.warn("please use provider \"aws-ec2\" instead of \"ec2\"");
      spec.setProvider("aws-ec2");
    }
    if (spec.getProvider().equals("cloudservers")){
      LOG.warn("please use provider \"cloudservers-us\" instead of \"cloudservers\"");
      spec.setProvider("cloudservers-us");
    }
    return new ComputeServiceContextFactory().createContext(spec.getProvider(),
      spec.getIdentity(), spec.getCredential(),
      wiring, ConfigurationConverter.getProperties(jcloudsConfig));
  }
}
