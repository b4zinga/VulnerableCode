package com.example.vulnerablecode.controller;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spel")
public class SpelController {

    /**
     * 存在SPEL表达式注入漏洞，攻击者传入 ?name=T(java.lang.Runtime).getRuntime().exec('calc') 即可执行calc命令
     * @param name
     * @return
     */
    @GetMapping("1")
    public String spel(String name) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(name);
        return expression.getValue().toString();
    }

    /**
     * 修复SPEL表达式注入漏洞，使用SimpleEvaluationContext防止SPEL注入
     * @param name
     * @return
     */
    @GetMapping("safe")
    public String safeSpel(String name) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(name);
        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        return expression.getValue(context).toString();
    }
}
