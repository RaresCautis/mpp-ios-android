import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet weak var dropDown1: UIPickerView!
    @IBOutlet weak var dropDown2: UIPickerView!
    @IBOutlet weak var arrivalTime: UILabel!
    
    private var stations:[String]=[String]()
    

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        self.dropDown1.delegate = self
        self.dropDown1.dataSource = self
        
        self.dropDown2.delegate = self
        self.dropDown2.dataSource = self
        
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView:UIPickerView, numberOfRowsInComponent component:Int)-> Int{
        return stations.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int)->String?{
        return stations[row]
    }
    
    @IBAction func onButtonTapped(){
        let originStation = dropDown1.selectedRow(inComponent:0)
        let finalStation = dropDown2.selectedRow(inComponent:0)
        presenter.makeTrainSearch(origin: Int32(originStation), destination: Int32(finalStation))
    }
    
    func setArrivalTimeLabel(text: String) {
        arrivalTime.text = text
    }
}

extension ViewController: ApplicationContractView {
    func setStationNames(stationNames: Array<String>) {
        stations = stationNames
    }
}
