<?php

use App\Models\Usek;
use Illuminate\Database\Seeder;

class UsekSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $useky = [
            [
                'zaciatocny_bod' => '45.387',
                'koncovy_bod' => '45.455',
                'cesta_id' => 2

            ],
            [
                'zaciatocny_bod' => '47.387',
                'koncovy_bod' => '47.455',
                'cesta_id' => 2

            ],
            [
                'zaciatocny_bod' => '40.387',
                'koncovy_bod' => '40.455',
                'cesta_id' => 2

            ]
        ];

        foreach ($useky as $usek) {
            Usek::create(array(
                'zaciatocny_bod' => $usek['zaciatocny_bod'],
                'koncovy_bod' => $usek['koncovy_bod'],
                'cesta_id' => $usek['cesta_id'],
            ));
        }
    }
}
