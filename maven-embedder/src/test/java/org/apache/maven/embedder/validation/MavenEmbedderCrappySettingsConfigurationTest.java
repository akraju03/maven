package org.apache.maven.embedder.validation;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.embedder.AbstractCoreMavenComponentTestCase;
import org.apache.maven.embedder.Configuration;
import org.apache.maven.embedder.ConfigurationValidationResult;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.SimpleConfiguration;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;

public class MavenEmbedderCrappySettingsConfigurationTest
    extends AbstractCoreMavenComponentTestCase
{
    public void testEmbedderWillStillStartupWhenTheSettingsConfigurationIsCrap()
        throws Exception
    {
        File projectDirectory = getTestFile( "src/examples/simple-project" );

        File user = new File( projectDirectory, "invalid-settings.xml" );

        Configuration configuration = new SimpleConfiguration()
            .setUserSettingsFile( user );

        ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration( configuration );

        assertFalse( validationResult.isValid() );

        MavenEmbedder embedder = new MavenEmbedder( configuration );

        MavenExecutionRequest request = createMavenExecutionRequest( new File( projectDirectory, "pom.xml" ) );        
        
        MavenExecutionResult result = embedder.execute( request );
                
        assertFalse( result.hasExceptions() );

        assertNotNull( result.getProject() );

        MavenProject project = result.getProject();

        String environment = project.getProperties().getProperty( "environment" );

        assertEquals( "development", environment );
    }

    @Override
    protected String getProjectsDirectory()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
