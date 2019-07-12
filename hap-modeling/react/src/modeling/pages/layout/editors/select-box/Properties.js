import React, { Component } from 'react';
import Properties from '../select/Properties';

export default function () {
  return [
    ...Properties(),
    'vertical',
  ];
}
