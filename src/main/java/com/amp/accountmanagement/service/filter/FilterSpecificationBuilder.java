package com.amp.accountmanagement.service.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

public class FilterSpecificationBuilder<T> {

  public Specification<T> createSpecification(ComparisonNode comparisonNode) {
    return Specification.where(
        new FilterSpecification<>(
            comparisonNode.getSelector(),
            comparisonNode.getOperator(),
            comparisonNode.getArguments()));
  }

  public Specification<T> createSpecification(LogicalNode logicalNode) {
    final List<Specification<T>> specifications = new ArrayList<>();

    logicalNode
        .getChildren()
        .forEach(node -> createSpecification(node).ifPresent(specifications::add));

    Specification<T> specification = specifications.get(0);
    if (logicalNode.getOperator() == LogicalOperator.AND) {
      for (int i = 1; i < specifications.size(); i++) {
        specification = Specification.where(specification).and(specifications.get(i));
      }
    }
    else if (logicalNode.getOperator() == LogicalOperator.OR) {
      for (int i = 1; i < specifications.size(); i++) {
        specification = Specification.where(specification).or(specifications.get(i));
      }
    }

    return specification;
  }

  private Optional<Specification<T>> createSpecification(Node node) {
    Optional<Specification<T>> specification = Optional.empty();

    if (node instanceof LogicalNode) {
      specification = Optional.of(createSpecification((LogicalNode) node));
    }
    if (node instanceof ComparisonNode) {
      specification = Optional.of(createSpecification((ComparisonNode) node));
    }

    return specification;
  }
}
