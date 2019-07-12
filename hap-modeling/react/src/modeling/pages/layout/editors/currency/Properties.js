import React, { Component } from 'react';
import Properties from '../number-field/Properties';

export default function () {
  return [
    ...Properties(),
    'currency',
  ];
}
