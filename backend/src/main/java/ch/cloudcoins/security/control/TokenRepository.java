package ch.cloudcoins.security.control;

import ch.cloudcoins.BaseRepository;
import ch.cloudcoins.security.entity.Token;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityGraph;

@ApplicationScoped
public class TokenRepository extends BaseRepository<Token> {

    public Token findByTokenString(String tokenString) {
        EntityGraph<Token> graph = getGraph();
        graph.addSubgraph("account");

        return singleResult(createNamedQueryWithGraph("Token.findByTokenString", graph).setParameter("token", tokenString));
    }
}
