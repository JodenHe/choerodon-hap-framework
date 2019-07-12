import Row from './row';
import Table from './table';
import TableItem from './table-column';
import TableQueryField from './table-query-field';
import TableButton from './table-button';
import Form from './form';
import FormItem from './form-field';
import TableCommand from './table-command';

const mapping = {
  Row,
  Table,
  TableItem,
  TableButton,
  TableCommand,
  TableQueryField,
  Form,
  FormItem,
};

export function getDesigner(type) {
  return mapping[type].designer;
}

export function getDefaultProps(type, dataSet) {
  return mapping[type].defaultProps(dataSet);
}

export function getProperties(type) {
  return mapping[type].properties;
}
