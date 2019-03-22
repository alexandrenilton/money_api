package com.abelem.money.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.model.Lancamento_;
import com.abelem.money.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryQueryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {
		/** usar criteria do javax.persistence, pois a do hibernate ficou depreciada */
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = builder.createQuery(Lancamento.class);

		// add filters
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		// add where conditions
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteriaQuery.where(predicates);

		// execute
		TypedQuery<Lancamento> query = entityManager.createQuery(criteriaQuery);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter filter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!StringUtils.isEmpty(filter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get(Lancamento_.DESCRICAO)), 
											"%" + filter.getDescricao().toLowerCase() + "%"));
		}

		if (filter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), filter.getDataVencimentoDe() ));
		}

		if (filter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), filter.getDataVencimentoAte() ));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return entityManager.createQuery(criteria).getSingleResult();
	}
}
