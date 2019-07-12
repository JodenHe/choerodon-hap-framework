import React, { Component } from 'react';
import Properties from '../text-field/Properties';

export default function () {
  return [
    ...Properties(),
    'max',
    'min',
    'step',
  ];
}
