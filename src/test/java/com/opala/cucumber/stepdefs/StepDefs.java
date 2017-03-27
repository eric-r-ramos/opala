package com.opala.cucumber.stepdefs;

import com.opala.OpalaApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = OpalaApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
