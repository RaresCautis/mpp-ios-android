import UIKit
import SharedCode

class TableViewCell: UITableViewCell {
    
    @IBOutlet weak var departureLabel: UILabel!
    @IBOutlet weak var arrivalLabel: UILabel!
}

class ViewController: UIViewController, ApplicationContractView {

    @IBOutlet weak var departureDropDown: UIPickerView!
    @IBOutlet weak var arrivalDropDown: UIPickerView!
    @IBOutlet weak var tableView: UITableView!
    
    private let tableViewCell: String = "TableViewCell"
    
    private var tableData: [DepartureInformation] = [DepartureInformation]()
    
    private var stations: [String] = [String]()
    

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        setUpPickers()
        setUpTable()
    }
    
    
    @IBAction func onButtonTapped() {
        let originStation = stations[departureDropDown.selectedRow(inComponent:0)]
        let finalStation = stations[arrivalDropDown.selectedRow(inComponent:0)]
        presenter.makeTrainSearch(originCrs: originStation, destinationCrs: finalStation)
    }
}

extension ViewController {
    func setStationNames(stationNames: Array<String>) {
        stations = stationNames
    }
}

extension ViewController: UIPickerViewDelegate, UIPickerViewDataSource {
    
    func setUpPickers() {
        self.departureDropDown.delegate = self
        self.departureDropDown.dataSource = self
        
        self.arrivalDropDown.delegate = self
        self.arrivalDropDown.dataSource = self
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView:UIPickerView, numberOfRowsInComponent component:Int) -> Int {
        return stations.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stations[row]
    }
}


extension ViewController: UITableViewDataSource, UITableViewDelegate {
    
    func setUpTable() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        registerTableViewCells()
    }
    
    func registerTableViewCells() {
        let trainCell = UINib(nibName: tableViewCell, bundle: nil)
        self.tableView.register(trainCell, forCellReuseIdentifier: tableViewCell)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) ->
        UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: tableViewCell, for: indexPath) as? TableViewCell{
            cell.departureLabel.text = tableData[indexPath.row].departureTime
            cell.arrivalLabel.text = tableData[indexPath.row].arrivalTime
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func setTableData(data: [DepartureInformation]) {
        tableData = data
        self.tableView.reloadData()
    }
}

extension ViewController{
    
    func createAlert(alertMessage: String){
        let alert = UIAlertController(title: "Error", message: alertMessage, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Okay", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}
