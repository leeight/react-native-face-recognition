
Pod::Spec.new do |s|
  s.name         = "RNFaceRecognition"
  s.version      = "1.0.0"
  s.summary      = "RNFaceRecognition"
  s.description  = <<-DESC
                  RNFaceRecognition
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNFaceRecognition.git", :tag => "master" }
  s.source_files  = "RNFaceRecognition/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  