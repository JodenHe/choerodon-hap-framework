const config = {
  master: '@choerodon/pro-master',
  projectType: 'hap',
  buildType: 'single',
  resourcesLevel: ['site', 'user'],
  modules: [
    '../target/generate-react/choerodon-fnd-util',
  ],
};

module.exports = config;
