// import './index.css';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Index = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3"></Col>
    </Row>
  );
};
