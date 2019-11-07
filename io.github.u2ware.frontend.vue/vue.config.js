module.exports = {
  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false
    },
    moment: {
      locales: [
        ''
      ]
    },
    // configureWebpack: {
    //     plugins: [
    //         new webpack.ProvidePlugin({
    //             $: 'jquery',
    //             jQuery: 'jquery',
    //             'window.jQuery': 'jquery',
    //         })
    //     ]
    // }
  }
}
