/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.notifygru.modules.forms.services.provider;

import fr.paris.lutece.plugins.forms.business.Form;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManager;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManagerHome;
import fr.paris.lutece.plugins.notifygru.modules.forms.services.NotifyGruFormsService;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.IProvider;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.NotifyGruMarker;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.ProviderManagerUtil;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.plugins.notifygru.modules.forms.services.INotifyGruFormsService;

/**
 * This class represents a provider for a {@link Forms} object
 *
 */
public class FormsProvider implements IProvider
{
    // PROPERTY KEY
    private static final String PROPERTY_SMS_SENDER_NAME = "workflow-notifygruforms.gruprovider.sms.sendername";

    // MARKS
    private static final String MARK_POSITION = "position_";

    // SERVICES
    private static INotifyGruFormsService _notifyGruFormsService = SpringContextService.getBean( NotifyGruFormsService.BEAN_SERVICE );

    // FIELDS
    private final String _strCustomerEmail;
    private final String _strConnectionId;
    private final String _strCustomerId;
    private final String _strCustomerPhoneNumber;
    private final String _strDemandReference;
    private final String _strDemandTypeId;
    private final FormResponse _formResponse;
    private final NotifygruMappingManager _mapping;

    /**
     * Constructor
     * 
     * @param strProviderManagerId
     *            the provider manager id. Used to retrieve the mapping.
     * @param strProviderId
     *            the provider id. Corresponds to the {@code Forms} id. Used to retrieve the mapping.
     * @param resourceHistory
     *            the resource history. Corresponds to the {@link Record} object containing the data to provide
     */
    public FormsProvider( String strProviderManagerId, String strProviderId, ResourceHistory resourceHistory )
    {
        //Get the form response from the resourceHistory
        _formResponse = FormResponseHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

        //Load the mapping manager
        _mapping = NotifygruMappingManagerHome.findByPrimaryKey( ProviderManagerUtil.buildCompleteProviderId( strProviderManagerId,
                strProviderId ) );

        if ( _mapping == null )
        {
            throw new AppException( "No mapping found for the form " + _formResponse.getFormId()
                    + ". Please check the configuration of the module-forms-mappingmanager." );
        }

        _strCustomerEmail = _notifyGruFormsService.getEmail( _mapping, _formResponse );
        _strConnectionId = _notifyGruFormsService.getConnectionId( _mapping, _formResponse );
        _strCustomerId = _notifyGruFormsService.getCustomerId( _mapping, _formResponse );
        _strCustomerPhoneNumber = _notifyGruFormsService.getSMSPhoneNumber( _mapping, _formResponse );
        _strDemandReference = _notifyGruFormsService.getDemandReference( _mapping, _formResponse );
        _strDemandTypeId = String.valueOf( _notifyGruFormsService.getIdDemandType( _mapping ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandId( )
    {
        return String.valueOf( _formResponse.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandTypeId( )
    {
        return _strDemandTypeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandSubtypeId( )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandReference( )
    {
        return _strDemandReference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerConnectionId( )
    {
        return _strConnectionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerId( )
    {
        return _strCustomerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerEmail( )
    {
        return _strCustomerEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideSmsSender( )
    {
        return AppPropertiesService.getProperty( PROPERTY_SMS_SENDER_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerMobilePhone( )
    {
        return _strCustomerPhoneNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<NotifyGruMarker> provideMarkerValues( )
    {
        Collection<NotifyGruMarker> result = new ArrayList<>( );

        List<FormQuestionResponse> listFormQuestionResponse = _notifyGruFormsService.getListFormQuestionResponse( _formResponse );

        for ( FormQuestionResponse formQuestionResponse : listFormQuestionResponse )
        {
            NotifyGruMarker notifyGruMarker = new NotifyGruMarker( MARK_POSITION + formQuestionResponse.getIdQuestion( ) );
            notifyGruMarker.setValue( ResponseHome.findByPrimaryKey( formQuestionResponse.getId( ) ).getToStringValueResponse() );
            result.add( notifyGruMarker );
        }

        return result;
    }

    /**
     * Get the collection of NotifyGruMarker, for the given form
     * @param form
     *          The form
     * @return the collection of the notifyGruMarkers
     */
    public static Collection<NotifyGruMarker> getProviderMarkerDescriptions( Form form )
    {
        Collection<NotifyGruMarker> collectionNotifyGruMarkers = new ArrayList<>( );

        List<Question> listFormQuestions = QuestionHome.getListQuestionByIdForm( form.getId( ) );

        for ( Question formQuestion : listFormQuestions )
        {
            NotifyGruMarker notifyGruMarker = new NotifyGruMarker( MARK_POSITION + formQuestion.getId( ) );
            notifyGruMarker.setDescription( formQuestion.getTitle( ) );
            collectionNotifyGruMarkers.add( notifyGruMarker );
        }
        
        return collectionNotifyGruMarkers;
    }

    /**
     * Ghe the reference list of the questions / positions for the given provider id
     * @param strProviderId
     *          The provider id
     * @return the reference list of the questions / positions. 
     */
    public static ReferenceList getQuestionPositions( String strProviderId )
    {
        return QuestionHome.getQuestionsReferenceListByForm( Integer.parseInt(strProviderId) );
    }

}