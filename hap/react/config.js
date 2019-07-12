const config = {
  // proxyTarget: 'http://hap4.staging.saas.hand-china.com',
  master: '@choerodon/pro-master',
  projectType: 'hap',
  buildType: 'single',
  resourcesLevel: ['site', 'user'],
  modules: [
    '../target/generate-react/choerodon-fnd-util',
    '../target/generate-react/hap-attachment',
    '../target/generate-react/hap-core',
    '../target/generate-react/hap-gateway',
    '../target/generate-react/hap-interface',
    '../target/generate-react/hap-job',
    '../target/generate-react/hap-mail',
    '../target/generate-react/hap-oauth2',
    '../target/generate-react/hap-report',
    '../target/generate-react/hap-task',
    '../target/generate-react/hap-workflow',
  ],
};

module.exports = config;
