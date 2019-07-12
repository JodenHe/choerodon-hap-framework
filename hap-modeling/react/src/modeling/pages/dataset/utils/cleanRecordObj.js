/* eslint-disable no-underscore-dangle */
const defaultDeleteProps = [
  '__dirty',
  '__id',
  '__status',
  '_token',
  'id',
];

const unique = a => [...new Set(a)];

export default function cleanRecordObj(obj, props = [], combine = true) {
  const reslutDeleteProps = combine
    ? unique(defaultDeleteProps.concat(props))
    : props;
  reslutDeleteProps.forEach((prop) => {
    if (obj[prop]) {
      delete obj[prop];
    }
  });
  return obj;
}
