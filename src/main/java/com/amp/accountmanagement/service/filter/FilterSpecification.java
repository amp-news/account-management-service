package com.amp.accountmanagement.service.filter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.data.jpa.domain.Specification;

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.IN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_IN;
import static java.util.Objects.isNull;

public class FilterSpecification<T> implements Specification<T> {

  private String property;
  private ComparisonOperator operator;
  private List<String> arguments;

  public FilterSpecification(String property, ComparisonOperator operator, List<String> arguments) {
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    final List<Object> arguments = castArguments(root.get(property).getJavaType());

    Predicate predicate = null;
    if (operator.equals(EQUAL)) {
      predicate = equalPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(NOT_EQUAL)) {
      predicate = notEqualPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(GREATER_THAN)) {
      predicate = greaterThanPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(GREATER_THAN_OR_EQUAL)) {
      predicate = greaterThanOrEqualPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(LESS_THAN)) {
      predicate = lessThanPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(LESS_THAN_OR_EQUAL)) {
      predicate = lessThanOrEqualPredicate(root, builder, arguments.get(0));
    } else if (operator.equals(IN)) {
      predicate = inPredicate(root, arguments);
    } else if (operator.equals(NOT_IN)) {
      predicate = notInPredicate(root, builder, arguments);
    }

    return predicate;
  }

  private Predicate inPredicate(Root<T> root, List<Object> arguments) {
    return root.get(property).in(arguments);
  }

  private Predicate notInPredicate(Root<T> root, CriteriaBuilder builder, List<Object> arguments) {
    return builder.not(root.get(property).in(arguments));
  }

  private Predicate lessThanOrEqualPredicate(
      Root<T> root, CriteriaBuilder builder, Object argument) {
    return builder.lessThanOrEqualTo(root.get(property), argument.toString());
  }

  private Predicate lessThanPredicate(Root<T> root, CriteriaBuilder builder, Object argument) {
    return builder.lessThan(root.get(property), argument.toString());
  }

  private Predicate greaterThanOrEqualPredicate(
      Root<T> root, CriteriaBuilder builder, Object argument) {
    return builder.greaterThanOrEqualTo(root.get(property), argument.toString());
  }

  private Predicate greaterThanPredicate(Root<T> root, CriteriaBuilder builder, Object argument) {
    return builder.greaterThan(root.get(property), argument.toString());
  }

  private Predicate notEqualPredicate(Root<T> root, CriteriaBuilder builder, Object argument) {
    Predicate predicate = builder.notEqual(root.get(property), argument);

    if (argument instanceof String) {
      return builder.notLike(root.get(property), argument.toString().replace('*', '%'));
    } else if (isNull(argument)) {
      return builder.isNotNull(root.get(property));
    }

    return predicate;
  }

  private Predicate equalPredicate(Root<T> root, CriteriaBuilder builder, Object argument) {
    Predicate predicate = builder.equal(root.get(property), argument);

    if (argument instanceof String) {
      predicate = builder.like(root.get(property), argument.toString().replace('*', '%'));
    } else if (isNull(argument)) {
      predicate = builder.isNull(root.get(property));
    }

    return predicate;
  }

  private List<Object> castArguments(Class<?> type) {
    final List<Object> preparedArguments = new ArrayList<>();

    for (final String argument : this.arguments) {
      if (type.equals(Integer.class)) {
        preparedArguments.add(Integer.parseInt(argument));
      } else if (type.equals(Long.class)) {
        preparedArguments.add(Long.parseLong(argument));
      } else {
        preparedArguments.add(argument);
      }
    }

    return preparedArguments;
  }
}
