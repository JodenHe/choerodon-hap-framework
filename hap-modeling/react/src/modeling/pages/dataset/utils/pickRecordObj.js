export default function pickRecordObj(obj, props = [], combine = true) {
  const res = {};
  props.forEach((prop) => {
    if (obj[prop]) {
      res[prop] = obj[prop];
    }
  });
  return res;
}
