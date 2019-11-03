class MyCustomUploadAdapter {

  constructor(loader) {
    this.loader = loader;
  }

  async upload() {

    return new Promise((resolve, reject) => {
      resolve({
        default : 'https://t1.daumcdn.net/cfile/tistory/99CB5D395D710DD91E',
        //40 : 'https://t1.daumcdn.net/cfile/tistory/99CB5D395D710DD91E',
      })
    });
  }

  abort() {
    alert('asdf!!!!!');
  }
}

function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
      // Configure the URL to the upload script in your back-end here!
      return new MyCustomUploadAdapter(loader);
    };
  }
  
export default MyCustomUploadAdapterPlugin;
  