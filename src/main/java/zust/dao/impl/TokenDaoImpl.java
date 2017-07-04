package zust.dao.impl;

import org.springframework.stereotype.Repository;

import zust.dao.TokenDaoI;
import zust.entity.Token;

@Repository
public class TokenDaoImpl extends BaseDaoImpl<Token> implements TokenDaoI{

}
