using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Face.Recognition.RNFaceRecognition
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNFaceRecognitionModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNFaceRecognitionModule"/>.
        /// </summary>
        internal RNFaceRecognitionModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNFaceRecognition";
            }
        }
    }
}
